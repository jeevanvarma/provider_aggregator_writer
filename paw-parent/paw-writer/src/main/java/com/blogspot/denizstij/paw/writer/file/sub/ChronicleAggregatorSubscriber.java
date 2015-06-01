package com.blogspot.denizstij.paw.writer.file.sub;


import java.io.IOException;

import net.openhft.chronicle.Chronicle;
import net.openhft.chronicle.ChronicleQueueBuilder;
import net.openhft.chronicle.ExcerptTailer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blogspot.denizstij.paw.common.state.IState;
import com.blogspot.denizstij.paw.common.state.StopState;
import com.blogspot.denizstij.paw.writer.file.IWriter;
/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public class ChronicleAggregatorSubscriber implements IAggregatorSubscriber {	
	
	private static final Logger logger = LoggerFactory.getLogger(ChronicleAggregatorSubscriber.class);
	private final String basePath = System.getProperty("java.io.tmpdir") + "/aggregator-subscriber";
	private Chronicle chronicle;
	private IState state= new StopState(this);	
	private IWriter writer;
	private Thread subChronicleReadThread;
	private String hostname="localhost";
	private int port=1234;
	
	@Override
	public String getName() {		
		return "Chronical Aggregator Subscriber";
	}

	@Override
	public void setHandle(IState state) {
		this.state=state;		
	}

	@Override
	public void start() {
		state.start();
	}	

	@Override
	public void handleStart() {
		try {
			chronicle = ChronicleQueueBuilder.statelessSink()
				  //  .indexed(basePath + "/statefull")
				    //.sink()
				    .connectAddress(hostname, port)
				    .build();
		} catch (IOException e) {
			state=new StopState(this);
			logger.error("Can not create chronicle source",e);
			return;
		}
		
		Runnable readRunnable = createReadRunnable();
		subChronicleReadThread = new Thread(readRunnable, "Chronicle aggregation sub thread");
		subChronicleReadThread.start();
	}

	private Runnable createReadRunnable(){
		return new Runnable() {
			
			@Override
			public void run() {
				ExcerptTailer reader=null;
				try {
					reader = chronicle.createTailer();
				} catch (IOException e) {
					logger.error("Can not read from chronicle",e);
					return;
				}
				while (state.isStarted()){			
					// Obtain an ExcerptTailer
					
					try {
												
						// While until there is a new Excerpt to read
						while(!reader.nextIndex());		
						// Read the object
						Object event = reader.readObject();		
						// Make the reader ready for next read						
						writer.onEvent((String) event);
					} catch (Exception e) {
						logger.error("Can not read from chronicle",e);
					}
					
				}
				reader.finish();
			}
		};
	}

	@Override
	public void stop() {
		state.stop();		
	}
	
	@Override
	public void handleStop() {
		try {
			chronicle.close();
		} catch (IOException e) {
			logger.error("Could not close chronicle source",e);
		}		
	}

	public IWriter getWriter() {
		return writer;
	}

	public void setWriter(IWriter writer) {
		this.writer = writer;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}	
}
