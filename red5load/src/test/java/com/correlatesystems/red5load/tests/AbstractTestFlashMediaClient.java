package com.correlatesystems.red5load.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.correlatesystems.red5load.Client;
import com.correlatesystems.red5load.impl.ClientMode;

/**
 * Common test implementations between clients
 * 
 * @author revprez
 *
 */
public abstract class AbstractTestFlashMediaClient {

    private static final Log log = LogFactory.getLog(AbstractTestFlashMediaClient.class);
	
    protected Client client;
    
	@Before
	public abstract void setUp();
	

	@Test
    public void testClientAvailable() {
        log.debug("@Test testRTMPClientAvailable");
        assertNotNull(client);
    }

    @Test
    public void testClientRecord() throws Exception {
    	client.setPublishName("testClientRecord");
    	String filename = this.getClass().getResource("/barsandtone.flv").getFile();
    	log.debug("testClientRecord() filename: " + filename);
    	client.setTimeOut(10000);
    	client.setMedia(filename);
    	client.setMode(ClientMode.RECORD);
    	Thread clientThread = new Thread(client);
    	
    	clientThread.start();
    	
    	while(  !client.succeeded() &&
    			!client.inError() 	&&
    			!client.timedOut() ) { 
    		log.debug("Waiting...");
    		Thread.sleep(1000);
    	}
    	
    	assertFalse(
    			"Did client beat the timeout?",
    			client.timedOut());
    	
    	assertFalse(
    			"Did client finish in error?",
    			client.inError());
    	
    	assertTrue(
    			"Did we publish?",
    			client.succeeded());
    }

	@After
	public abstract void tearDown();
	

}
