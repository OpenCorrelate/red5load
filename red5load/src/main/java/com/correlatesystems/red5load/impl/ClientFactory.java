package com.correlatesystems.red5load.impl;

import com.correlatesystems.red5load.Client;

public class ClientFactory {

	public static Client getClient(String host, int port, String app) {
		// TODO Auto-generated method stub
		RTMPClient client = new RTMPClient();
		client.initialize(host, port, app);
		return client;
	}

}
