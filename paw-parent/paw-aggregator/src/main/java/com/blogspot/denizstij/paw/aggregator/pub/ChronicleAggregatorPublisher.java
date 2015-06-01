package com.blogspot.denizstij.paw.aggregator.pub;


import java.io.IOException;

import net.openhft.chronicle.Chronicle;
import net.openhft.chronicle.ChronicleQueueBuilder;
import net.openhft.chronicle.ExcerptAppender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blogspot.denizstij.paw.common.state.IState;
import com.blogspot.denizstij.paw.common.state.StopState;

/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public class ChronicleAggregatorPublisher implements IAggregatorPublisher{	
	
	private static final Logger logger = LoggerFactory.getLogger(ChronicleAggregatorPublisher.class);
	private final String basePath = System.getProperty("java.io.tmpdir") + "/aggregator-publisher/new";
	private Chronicle source;
	private IState state= new StopState(this);		
	private String hostname="localhost";
	private int port=1234;

	@Override
	public void publish(String data) {
		ExcerptAppender appender;
		try {
			appender = source.createAppender();
			appender.startExcerpt(); 
			appender.writeObject(data);
			appender.finish();
		} catch (IOException e) {
			logger.error("Could not publish data:"+data,e);
		}	
	}

	@Override
	public String getName() {		
		return "Aggregator Publisher (Chronical)";
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
	public void stop() {
		state.stop();		
	}
	

	@Override
	public void handleStart() {
		try {
			logger.info("Chronicle base path:"+basePath);
			source = ChronicleQueueBuilder
				    .indexed(basePath )				    
				    .source()
				    .bindAddress(hostname, port)
				    .build();
		} catch (IOException e) {
			state=new StopState(this);
			logger.error("Can not create chronicle source",e);
		}				
	}

	@Override
	public void handleStop() {
		try {
			source.close();
		} catch (IOException e) {
			logger.error("Could not close chronicle source",e);
		}		
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
