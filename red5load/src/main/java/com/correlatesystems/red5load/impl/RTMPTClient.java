package com.correlatesystems.red5load.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.red5.server.api.service.IPendingServiceCall;
import org.red5.server.net.rtmp.event.Notify;

import com.correlatesystems.red5load.BaseClient;
import com.correlatesystems.red5load.exceptions.InvalidMediaException;

public class RTMPTClient 
	extends org.red5.server.net.rtmpt.RTMPTClient
	implements BaseClient {

	private static final Log log = LogFactory.getLog(RTMPTClient.class);
	
	protected String host;
    protected int port;
    protected String app;
    protected int state;
    protected String publishName;
    protected int streamId;
    
    @Override
	public void initialize(String host, int port, String app) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStreamEvent(Notify arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resultReceived(IPendingServiceCall arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMedia(String string) throws InvalidMediaException {
		// TODO Auto-generated method stub
		
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
		return false;
	}

	@Override
	public boolean timedOut() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setTimeOut(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean inError() {
		// TODO Auto-generated method stub
		return false;
	}

}
