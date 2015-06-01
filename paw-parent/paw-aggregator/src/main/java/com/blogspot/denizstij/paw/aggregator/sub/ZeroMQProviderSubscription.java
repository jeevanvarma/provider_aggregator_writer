package com.blogspot.denizstij.paw.aggregator.sub;


import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import com.blogspot.denizstij.paw.aggregator.IAggregator;
import com.blogspot.denizstij.paw.common.MarketValue;
import com.blogspot.denizstij.paw.common.ProviderConfigInfo;
import com.blogspot.denizstij.paw.common.serializer.ISerializer;
import com.blogspot.denizstij.paw.common.serializer.SmileJacksonSerializer;

/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public class ZeroMQProviderSubscription implements IProviderSubscription{	
	private static final Logger logger = LoggerFactory.getLogger(ZeroMQProviderSubscription.class);
	private ZMQ.Context context = ZMQ.context(10);
	private ConcurrentHashMap<String,SubscriberRunnable > subscriptionMap= new ConcurrentHashMap<>();  
	private ISerializer serializer= new SmileJacksonSerializer();
	
	@Override
	public void subscribe(IAggregator aggregator,
			ProviderConfigInfo providerConfig) {
		String providerId = providerConfig.getProviderId();
		if (subscriptionMap.containsKey(providerId)){
			logger.warn("Already subscribed to :"+providerConfig);
			return;
		}
		
		Socket subscriber = context.socket(ZMQ.SUB);
		SubscriberRunnable subscriberRunnable= new SubscriberRunnable(subscriber,aggregator,providerConfig);
		SubscriberRunnable preSubscriberRunnable = subscriptionMap.putIfAbsent(providerId, subscriberRunnable);
		if (preSubscriberRunnable!=null){
			// there is already a connection happening... 
			return;
		}
		
		Thread thread= new Thread(subscriberRunnable, "ZeroMQ-Subscription Handler for provider:"+providerId);
		thread.start();		
	}

	@Override
	public void unsubscribe(IAggregator aggregator, ProviderConfigInfo providerConfig) {
		SubscriberRunnable subscriberRunnable=subscriptionMap.get(providerConfig.getProviderId());
		if (subscriberRunnable!=null){
			logger.info("Unsubscribing "+providerConfig);
			subscriptionMap.remove(providerConfig.getProviderId());
			subscriberRunnable.stop();
			
		}
	}
	
	public ISerializer getSerializer() {
		return serializer;
	}

	public void setSerializer(ISerializer serializer) {
		this.serializer = serializer;
	}

	class SubscriberRunnable implements Runnable {
		private volatile boolean start=false;
		private final Socket subscriber;
		private final IAggregator aggregator;
		private final ProviderConfigInfo providerConfig;

		public SubscriberRunnable(Socket subscriber, IAggregator aggregator,
				ProviderConfigInfo providerConfig) {
			this.subscriber=subscriber;
			this.aggregator=aggregator;
			this.providerConfig=providerConfig;
		}

		@Override
		public void run() {			
			logger.info("Subscribing to "+providerConfig);
			start=true;
			subscriber.connect(providerConfig.getConnectionUrl());
			subscriber.subscribe(providerConfig.getTopic().getBytes());
			logger.info("Subscribed to "+providerConfig);
			while (start) {
				// first message is the topic name always :(
				String topic=subscriber.recvStr();
				byte[] bytes = subscriber.recv();
				MarketValue event;
				try {
					event = getSerializer().deserialize(bytes);
				}  catch (Exception e) { 
					logger.error("Could not read message from buffer. Dropping it:"+DatatypeConverter.printHexBinary(bytes));
					return;
				}
				aggregator.onEvent(event);
	        }
		}
		
		public void stop(){			
			start=false;
			subscriber.unsubscribe(providerConfig.getTopic().getBytes());
			subscriber.close();			
		}		
	}	
}
