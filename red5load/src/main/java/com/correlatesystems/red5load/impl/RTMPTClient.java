package com.correlatesystems.red5load.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.red5.io.object.Deserializer;
import org.red5.io.object.Serializer;
import org.red5.io.utils.ObjectMap;
import org.red5.server.api.service.IPendingServiceCall;
import org.red5.server.net.rtmp.event.IRTMPEvent;
import org.red5.server.net.rtmp.event.Notify;
import org.red5.server.net.rtmp.status.StatusCodes;
import org.red5.server.net.rtmpt.codec.RTMPTCodecFactory;
import org.red5.server.stream.FileStreamSource;
import org.red5.server.stream.message.RTMPMessage;

import com.correlatesystems.red5load.BaseClient;
import com.correlatesystems.red5load.exceptions.InvalidMediaException;

public class RTMPTClient 
	extends org.red5.server.net.rtmpt.RTMPTClient
	implements BaseClient {

	private static final Log log = LogFactory.getLog(RTMPTClient.class);

	private static final int TIMEOUT = 10000;
	
	protected String host;
    protected int port;
    protected String app;
   

    protected int state;
    protected String publishName = "test";
    protected String mode = "live";
    protected int streamId;

    private long created = System.currentTimeMillis();
	private int timeOut = TIMEOUT;
    
	private boolean succeeded;

	private FileStreamSource source;
    
    @Override
	public void initialize(String host, int port, String app) {
        this.host = host;
        this.port = port;
        this.app = app;
        state = ClientState.STOPPED;
        setServiceProvider(this);
        RTMPTCodecFactory codecFactory = new RTMPTCodecFactory();
        codecFactory.setDeserializer(new Deserializer());
        codecFactory.setSerializer(new Serializer());
        codecFactory.init();
        setCodecFactory(codecFactory);
	}

    @Override
    public void onStreamEvent(Notify notify) { 
        log.debug("onStreamEvent - " + notify); 
        if (notify.getCall().getServiceMethodName().equals("onStatus")) { 

            ObjectMap<String, String> map = (ObjectMap<String, String>) notify.getCall().getArguments()[0]; 
            String code = map.get("code"); 
            

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
		source = MediaStreamingUtils.getFileStreamSource(filename); 
	}


    @Override
	public void setMode(String record) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPublishName(String publishName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		state = ClientState.CONNECTING;
	    connect(host, port, app, this);	
	}

	@Override
	public void stop() {
        if (state >= ClientState.STREAM_CREATING) {
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
