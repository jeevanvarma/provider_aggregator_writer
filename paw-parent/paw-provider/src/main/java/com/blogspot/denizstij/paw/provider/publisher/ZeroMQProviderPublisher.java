package com.blogspot.denizstij.paw.provider.publisher;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import com.blogspot.denizstij.paw.common.MarketValue;
import com.blogspot.denizstij.paw.common.ProviderConfigInfo;
import com.blogspot.denizstij.paw.common.serializer.ISerializer;
import com.blogspot.denizstij.paw.common.serializer.SmileJacksonSerializer;
import com.blogspot.denizstij.paw.common.state.Controllable;
import com.blogspot.denizstij.paw.common.state.IState;
import com.blogspot.denizstij.paw.common.state.StopState;

/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public class ZeroMQProviderPublisher implements IPublisher, Controllable{
	
	private static final Logger logger = LoggerFactory.getLogger(ZeroMQProviderPublisher.class);
	
	private IState states= new StopState(this);
	private ZMQ.Context context = ZMQ.context(10);	
	private ProviderConfigInfo providerConfigInfo;	
	private Socket publisher;
	private ISerializer serializer= new SmileJacksonSerializer();//new KryoSerializer();
	
	@Override
	public String getName() {
		return "Publisher for provider: "+providerConfigInfo;
	}
	
	@Override
	public void start() {
		states.start();
	}


	@Override
	public void stop() {
		states.stop();
	}	
	
	@Override
	public void handleStart() {
		try {
			logger.info("Starting to publish on " + providerConfigInfo);
			publisher = context.socket(ZMQ.PUB);
			publisher.connect(providerConfigInfo.getConnectionUrl());
			publisher.bind(providerConfigInfo.getConnectionUrl());
			publisher.setHWM(1000000);
			publisher.setSndHWM(1000000);
			logger.info("Ready to publish on " + providerConfigInfo);
		} catch (Exception e) {
			logger.error("Can not publish!", e);
			states.stop();
		}
	}

	@Override
	public void handleStop() {
		publisher.close();
		context.term();		
	}
	@Override
	public void publish(MarketValue marketValue) {
		if (!states.isStarted()){
			logger.debug("Not connected therefore not publishing message:"+marketValue);
		}
		
		publisher.sendMore(providerConfigInfo.getTopic());
		byte[] dst=null;
		try {
			dst = serializer.serialize(marketValue);
		} catch (Exception e) {
			logger.error("Could not write message to buffer. Dropping it:"+marketValue);
			return;
		}
		publisher.send(dst);
	}

	public ISerializer getSerializer() {
		return serializer;
	}


	public void setSerializer(ISerializer serializer) {
		this.serializer = serializer;
	}

	@Override
	public void setHandle(IState state) {
		this.states=state;		
	}

	public ProviderConfigInfo getProviderConfigInfo() {
		return providerConfigInfo;
	}

	public void setProviderConfigInfo(ProviderConfigInfo providerConfigInfo) {
		this.providerConfigInfo = providerConfigInfo;
	}	
}
