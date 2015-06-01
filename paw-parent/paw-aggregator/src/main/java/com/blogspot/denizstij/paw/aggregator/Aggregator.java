package com.blogspot.denizstij.paw.aggregator;

import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blogspot.denizstij.paw.aggregator.pub.IAggregatorPublisher;
import com.blogspot.denizstij.paw.aggregator.sub.IProviderSubscription;
import com.blogspot.denizstij.paw.common.MarketValue;
import com.blogspot.denizstij.paw.common.ProviderConfigInfo;
import com.blogspot.denizstij.paw.common.state.IState;
import com.blogspot.denizstij.paw.common.state.StopState;

/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public class Aggregator implements IAggregator {
	private static final Logger logger = LoggerFactory.getLogger(Aggregator.class);
	private IState state= new StopState(this);
	// structure of this map is never modified once it is created during start up
	// therefore it is OK to use non-thread-safe map. 
	private final TreeMap<String, AggregationBean> insturmentAggregationMap= new TreeMap<>();
	 
	private Set<ProviderConfigInfo> providerConfigInfo;
	private IProviderSubscription providerSubscription;
	private Thread  aggregatorThread;	
	private IAggregatorPublisher aggregatorPublisher;

	
	@Override
	public void onEvent(MarketValue event) {
		AggregationBean aggregationBean = insturmentAggregationMap.get(event.getName());
		if (aggregationBean==null){
			logger.debug("Event does not have a valid insturement published by any of the registered providers: "+event.getName());
			return;
		}		
		aggregationBean.updateValue(event);
	}


	private class AggregatorRunnable implements Runnable{

		@Override
		public void run() {
			while (state.isStarted()) {
				loopOfHolyGrail();			
				publishAggregationStates();
			}
		}
		// The aggregator contains a dependency while processing messages which introduces a 1 second delay to your processing loop.
		// Simulating this logic with a sleep 
		private void loopOfHolyGrail() {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
					logger.error("Something woke me up :(",e);
			}		
		}

		public void publishAggregationStates() {
			StringBuilder sb = new StringBuilder(1024);

			Set<Entry<String, AggregationBean>> entrySet = insturmentAggregationMap.entrySet();
			for (Entry<String, AggregationBean> entry : entrySet) {
				String instrument=entry.getKey();
				AggregationBean aggregationBean = entry.getValue();
				if (aggregationBean.isAggregationStateChanged()){
					double aggValue = aggregationBean.getAggValue();
					sb.append(String.format("%s,%.2f\n", instrument,aggValue));
				}
			}
			if (sb.length()!=0){				
				getAggregatorPublisher().publish(sb.toString());
			}
		}
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
		for (ProviderConfigInfo providerConfig:providerConfigInfo){
			addSymbolListingCounterMap(providerConfig);
			getProviderSubscription().subscribe(this,providerConfig);			
		}
		aggregatorPublisher.start();
		aggregatorThread= new Thread(new AggregatorRunnable(), "Aggregator");
		aggregatorThread.start();
	}

	@Override
	public void handleStop() {
		aggregatorPublisher.stop();
		for (ProviderConfigInfo providerConfig:providerConfigInfo){
			getProviderSubscription().unsubscribe(this,providerConfig);		
		}		
	}

	// Note, this method is not thread safe, as this method would be called only from initialization/startup of aggregator
	private void addSymbolListingCounterMap(ProviderConfigInfo providerConfig) {
		Set<String> instrumentSet = providerConfig.getInstrumentSet();	
		for (String sym : instrumentSet) {
			AggregationBean aggregationBean = insturmentAggregationMap.get(sym);
			if (aggregationBean==null){
				aggregationBean= new AggregationBean(sym);
				insturmentAggregationMap.put(sym, aggregationBean);
			}
			aggregationBean.addProvider(providerConfig.getProviderId());			
		}
	}
	
	@Override
	public String getName() {		
		return "Aggregator";
	}

	public Set<ProviderConfigInfo> getProviderConfigInfo() {
		return providerConfigInfo;
	}

	public void setProviderConfigInfo(Set<ProviderConfigInfo> providerConfigInfo) {
		this.providerConfigInfo = providerConfigInfo;
	}

	public IProviderSubscription getProviderSubscription() {
		return providerSubscription;
	}

	public void setProviderSubscription(IProviderSubscription providerSubscription) {
		this.providerSubscription = providerSubscription;
	}

	@Override
	public void setHandle(IState state) {
		this.state=state;
	}

	public IAggregatorPublisher getAggregatorPublisher() {
		return aggregatorPublisher;
	}

	public void setAggregatorPublisher(IAggregatorPublisher aggregatorPublisher) {
		this.aggregatorPublisher = aggregatorPublisher;
	}	
}
