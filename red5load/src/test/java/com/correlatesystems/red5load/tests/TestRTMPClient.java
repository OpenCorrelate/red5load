package com.correlatesystems.red5load.tests;

import com.correlatesystems.red5load.Client;
import com.correlatesystems.red5load.impl.ClientFactory;
import com.correlatesystems.red5load.impl.ClientMode;
import com.correlatesystems.red5load.impl.RTMPClient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;


/**
 * Test RTMP client
 * 
 * @author revprez
 *
 */
public class TestRTMPClient extends AbstractTestFlashMediaClient {

    private static final Log log = LogFactory.getLog(TestRTMPClient.class);

    
    
    @BeforeClass
    public static void initialSetUp() {
        log.debug("@BeforeClass initialSetUp");
    }
    
    @Override
    public void setUp() {
        log.debug("@Before setUp");

        client = ClientFactory.getClient(
        		RTMPClient.class,
        		"exeter.local",
        		1935,
        		"oflaDemo");
    }
    
    @Override
    public void tearDown() {
        log.debug("@After tearDown");
    }

    @AfterClass
    public static void finalTearDown() {
        log.debug("@AfterClass finalTearDown");
        
    }

}
