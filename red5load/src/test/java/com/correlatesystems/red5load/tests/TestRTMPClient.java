package com.correlatesystems.red5load.tests;

import com.correlatesystems.red5load.Client;
import com.correlatesystems.red5load.Client;
import com.correlatesystems.red5load.impl.ClientFactory;
import com.correlatesystems.red5load.impl.ClientMode;
import com.correlatesystems.red5load.impl.RTMPClient;

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
public class TestRTMPClient {

    private static final Log log = LogFactory.getLog(TestRTMPClient.class);

    private Client client;
    
    @BeforeClass
    public static void initialSetUp() {
        log.debug("@BeforeClass initialSetUp");
    }
    
    @Before
    public void setUp() {
        log.debug("@Before setUp");

        client = ClientFactory.getClient(
        		"exeter.local",
        		1935,
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
