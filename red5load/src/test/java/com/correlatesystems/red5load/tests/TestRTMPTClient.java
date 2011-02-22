package com.correlatesystems.red5load.tests;

import com.correlatesystems.red5load.Client;
import com.correlatesystems.red5load.impl.ClientFactory;
import com.correlatesystems.red5load.impl.ClientMode;
import com.correlatesystems.red5load.impl.RTMPTClient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * Test clients
 * 
 * @author revprez
 *
 */
public class TestRTMPTClient {

    private static final Log log = LogFactory.getLog(TestRTMPTClient.class);

    private Client client;
    
    
    @Before
    public void setUp() {
        log.debug("@Before setUp");

        client = ClientFactory.getClient(
        		RTMPTClient.class,
        		"exeter.local",
        		8088,
        		"oflaDemo");
    }
    
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
    public void tearDown() {
        log.debug("@After tearDown");
    }

    @AfterClass
    public static void finalTearDown() {
        log.debug("@AfterClass finalTearDown");
        
    }
    
}
