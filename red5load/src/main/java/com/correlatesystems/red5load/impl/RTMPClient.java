package com.correlatesystems.red5load.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import com.correlatesystems.red5load.BaseClient;
import com.correlatesystems.red5load.exceptions.InvalidMediaException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.red5.server.api.service.IPendingServiceCall;
import org.red5.server.cache.NoCacheImpl;
import org.red5.io.ITagReader;
import org.red5.io.flv.IFLV;
import org.red5.io.flv.impl.FLVService;
import org.red5.io.object.Deserializer;
import org.red5.io.object.Serializer;
import org.red5.io.utils.ObjectMap;
import org.red5.server.net.rtmp.event.IRTMPEvent;
import org.red5.server.net.rtmp.event.Notify;
import org.red5.server.net.rtmp.status.StatusCodes;
import org.red5.server.stream.FileStreamSource;
import org.red5.server.stream.message.RTMPMessage;

/**
 * 
 * Red5Load implementation of {@link org.red5.server.net.rtmp.RTMPClient}
 * 
 * @author revprez
 *
 */
public class RTMPClient 
    extends org.red5.server.net.rtmp.RTMPClient
    implements BaseClient {
    
    private static final Log log = LogFactory.getLog(RTMPClient.class);

	private static final int TIMEOUT = 10000;

	protected String host;
    protected int port;
    protected String app;
   

    protected int state;
    protected String publishName = "test";
    protected String mode = "live";
    protected int streamId;

    protected boolean succeeded = false;
    
	protected FileStreamSource source;
	
	private long created = System.currentTimeMillis();
	private int timeOut = TIMEOUT;
    
	@Override
    public void initialize(String host, int port, String app) {
        this.host = host;
        this.port = port;
        this.app = app;
        state = ClientState.STOPPED;
        setServiceProvider(this);
    }
    
    @Override
    public void onStreamEvent(Notify notify) { 
        log.debug("onStreamEvent - " + notify); 
        if (notify.getCall().getServiceMethodName().equals("onStatus")) { 

            ObjectMap<String, String> map = (ObjectMap<String, String>) notify.getCall().getArguments()[0]; 
            String code = map.get("code"); 
            String description = map.get("description"); 
            String details = map.get("details"); 


            if (StatusCodes.NS_PUBLISH_START.equals(code)) { 
                
                try { 
                	
                	if (source == null) {
                		log.error("No source set to publish");
                		throw new InvalidMediaException("No source set to publish.");
                	}
                	
                	while (source.hasMore()) { 
	                	IRTMPEvent event = source.dequeue(); 
	                	RTMPMessage message  = new RTMPMessage(); 
	                	message.setBody(event); 
	                	publishStreamData(streamId, message); 
	                }
	                succeeded = true; 
	                disconnect(); 
	
	            } catch (Exception e) { 
	            	log.error("ERROR: " + e.getMessage());
	            	state = ClientState.ERROR;
	            	disconnect();
	            } 
            }
		    
		
		    if (StatusCodes.NS_PLAY_STOP.equals(code)) { 
		        log.debug("onStatus code == NetStream.Play.Stop, disconnecting"); 
		        disconnect(); 
		    }
		}
    } 

    @Override
    public void resultReceived(IPendingServiceCall call) {
        
        if ("connect".equals(call.getServiceMethodName())) {
            state = ClientState.STREAM_CREATING;
            createStream(this);
        } else if ("createStream".equals(call.getServiceMethodName())) {
            state = ClientState.PUBLISHING;
            Object result = call.getResult();
            if (result instanceof Integer) {
                Integer streamIdInt = (Integer) result;
                streamId = streamIdInt.intValue();
                publish(streamIdInt.intValue(), publishName, mode, this);
            } else {
            	disconnect();
                state = ClientState.STOPPED;
            }
        }
    }
    

	@Override
    public void run() {
        log.debug("running...");
        start();
    }

	@Override
	public void setMedia(String filename) throws InvalidMediaException {
		FLVService service = new FLVService(); 
        service.setSerializer(new Serializer()); 
        service.setDeserializer(new Deserializer()); 

        
        File f = new File(filename); 

        IFLV flv;
		try {
			flv = (IFLV) service.getStreamableFile(f);
		} catch (IOException e) {
			String ioMsg = e.getCause().toString();
			String invalidMediaMsg = String.format(
					"Unable to generate FLV from given filename: %s",
					f.getAbsolutePath());
			log.error(ioMsg);
			
			throw new InvalidMediaException(invalidMediaMsg);
			
		} 
		
        flv.setCache(NoCacheImpl.getInstance()); 

        ITagReader reader;
		try {
			reader = flv.getReader();
		} catch (IOException e) {
			String ioMsg = e.getCause().toString();
			String invalidMediaMsg = String.format(
					"Unable to grab reader for given flv: %s",
					f.getAbsolutePath());
			log.error(ioMsg);
			
			throw new InvalidMediaException(invalidMediaMsg);
		} 

        source = new FileStreamSource(reader); 
		
	}

	@Override
	public void setMode(String mode) {
		this.mode = mode;
		
	}

	@Override
	public void setPublishName(String publishName) {
		this.publishName = publishName;
		
	}

	@Override
    public void start() {
        state = ClientState.CONNECTING;
        connect(host, port, app, this);
    }
    
	@Override
    public void stop() {
		log.debug("stop state: " + state);
        if (state >= ClientState.STREAM_CREATING) {
        	log.debug("Disconnecting...");
            disconnect();
        }
        state = ClientState.STOPPED;
    }

	@Override
	public boolean succeeded() {
		// TODO Auto-generated method stub
		return succeeded;
	}

	@Override
	public boolean timedOut() {
		// TODO Auto-generated method stub
		long now = System.currentTimeMillis();
		boolean isTimedOut = (now - created) > timeOut;
		if (isTimedOut) {
			state = ClientState.ERROR;
			log.error("Client timed out...");
			disconnect();
		}
		return isTimedOut;
		
	}

	@Override
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	@Override
	public boolean inError() {
		// TODO Auto-generated method stub
		return state == ClientState.ERROR;
	}

}
