package com.correlatesystems.red5load;

import com.correlatesystems.red5load.exceptions.InvalidMediaException;

/**
 * Red5Load's Client interface
 * 
 * @author revprez
 *
 */
public interface Client extends Runnable {

	public void initialize(String host, int port, String app);
 
	public void setMedia(String string) throws InvalidMediaException;

	public void setMode(String record);
	
	public void setPublishName(String publishName);
	
	public boolean succeeded();

	public void stop();

	void start();

	public boolean timedOut();

	public void setTimeOut(int i);

	public boolean inError();

	

}
