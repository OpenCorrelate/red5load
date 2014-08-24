package com.correlatesystems.red5load.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.correlatesystems.red5load.Client;

public class ClientFactory {

    private static final Log log = LogFactory.getLog(ClientFactory.class);
    
	public static Client getClient(Class<? extends Client> clientClass, String host, int port, String app) {
		// TODO Auto-generated method stub
		Client client;
		try {
			client = clientClass.newInstance();
			client.initialize(host, port, app);
			return client;
		} catch (InstantiationException e) {
			log.error("Unable to instantiate: " + e.getMessage());
		} catch (IllegalAccessException e) {
			log.error("Unable to instantiate: " + e.getMessage());
		}
		
		return null;

	}

}
