package com.correlatesystems.red5load;

import java.lang.Runnable;
import java.util.Map;

import org.red5.server.api.event.IEventDispatcher;
import org.red5.server.api.service.IPendingServiceCallback;
import org.red5.server.messaging.IMessage;
import org.red5.server.net.rtmp.ClientExceptionHandler;
import org.red5.server.net.rtmp.INetStreamEventHandler;
import org.red5.server.net.rtmp.RTMPClientConnManager;
import org.red5.server.net.rtmp.RTMPConnection;
import org.red5.server.net.rtmp.codec.RTMP;
import org.red5.server.net.rtmp.codec.RTMPCodecFactory;

import com.correlatesystems.red5load.exceptions.InvalidMediaException;

/**
 * Red5Load's interface to {@link import org.red5.server.net.rtmp.BaseRTMPHandler}
 *  
 * @author revprez
 *
 */
public interface BaseClient 
	extends
		Client,
		INetStreamEventHandler,
		IPendingServiceCallback {

	
	public void connect(String server, int port, Map<String,Object> connectionParams); 
	
	public void connect(String server, int port, Map<String,Object> connectionParams, IPendingServiceCallback connectCallback);
	
	public void connect(String server, int port, Map<String,Object> connectionParams, IPendingServiceCallback connectCallback, Object[] connectCallArguments);
	
	public void connect(String server, int port, String application);
	
	public void connect(String server, int port, String application, IPendingServiceCallback connectCallback);
    
    public void	connectionClosed(RTMPConnection conn, RTMP state);
    
    public void	connectionOpened(RTMPConnection conn, RTMP state);
    
    public void	createStream(IPendingServiceCallback callback); 
     
    public void	disconnect(); 
    
    public RTMPClientConnManager getConnManager(); 
     
    public void	handleException(Throwable throwable); 
     
    public void invoke(String method, IPendingServiceCallback callback);
    
    public void invoke(String method, Object[] params, IPendingServiceCallback callback);
    
    public Map<String,Object> makeDefaultConnectionParams(String server, int port, String application);
    
     
    public void play(int streamId, String name, int start, int length); 
     
    public void publish(int streamId, String name, String mode, INetStreamEventHandler handler); 
     
    public void	publishStreamData(int streamId, IMessage message); 
     
    public void setCodecFactory(RTMPCodecFactory factory);

    public void setConnectionClosedHandler(Runnable connectionClosedHandler);
    
    public void	setExceptionHandler(ClientExceptionHandler exceptionHandler); 
     
    public void	setServiceProvider(Object serviceProvider); 
    
    public void	setStreamEventDispatcher(IEventDispatcher streamEventDispatcher);

    public void	unpublish(int streamId);
    
}
