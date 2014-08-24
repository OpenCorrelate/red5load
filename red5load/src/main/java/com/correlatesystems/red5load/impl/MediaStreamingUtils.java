package com.correlatesystems.red5load.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.red5.io.ITagReader;
import org.red5.io.flv.IFLV;
import org.red5.io.flv.impl.FLVService;
import org.red5.io.object.Deserializer;
import org.red5.io.object.Serializer;
import org.red5.server.api.service.IPendingServiceCall;
import org.red5.server.cache.NoCacheImpl;
import org.red5.server.stream.FileStreamSource;

import com.correlatesystems.red5load.exceptions.InvalidMediaException;

/**
 * Static and instance mixin for pushing and pulling streams
 *  
 * @author revprez
 *
 */
public class MediaStreamingUtils {

    private static final Log log = LogFactory.getLog(MediaStreamingUtils.class);
    
	public static FileStreamSource getFileStreamSource(String filename) throws InvalidMediaException {
		// TODO Auto-generated method stub
		FLVService service = new FLVService(); 
        service.setSerializer(new Serializer()); 
        service.setDeserializer(new Deserializer()); 

        
        File f = new File(filename); 

        IFLV flv;
		try {
			flv = (IFLV) service.getStreamableFile(f);
		} catch (IOException e) {
			String ioMsg = e.getCause().toString();
			String invalidMediaMsg = String.format(
					"Unable to generate FLV from given filename: %s",
					f.getAbsolutePath());
			log.error(ioMsg);
			
			throw new InvalidMediaException(invalidMediaMsg);
			
		} 
		
        flv.setCache(NoCacheImpl.getInstance()); 

        ITagReader reader;
		try {
			reader = flv.getReader();
		} catch (IOException e) {
			String ioMsg = e.getCause().toString();
			String invalidMediaMsg = String.format(
					"Unable to grab reader for given flv: %s",
					f.getAbsolutePath());
			log.error(ioMsg);
			
			throw new InvalidMediaException(invalidMediaMsg);
		} 
		return new FileStreamSource(reader);
	}

}
