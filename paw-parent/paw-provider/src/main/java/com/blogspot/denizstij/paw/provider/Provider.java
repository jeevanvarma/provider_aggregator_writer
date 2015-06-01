package com.blogspot.denizstij.paw.provider;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.blogspot.denizstij.paw.common.MarketValue;
import com.blogspot.denizstij.paw.common.ProviderConfigInfo;
import com.blogspot.denizstij.paw.common.state.IState;
import com.blogspot.denizstij.paw.common.state.StopState;
import com.blogspot.denizstij.paw.provider.datasource.IMarketValueDataSource;
import com.blogspot.denizstij.paw.provider.publisher.IPublisher;

/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public class Provider implements IProvider {	
	
	private IState states= new StopState(this);
	private ProviderConfigInfo providerConfigInfo;
	private IPublisher publisher;
		
	private IMarketValueDataSource marketValueDataSource;
	private Executor executorService;
	
	@Override
	public String getProviderId() {
		return providerConfigInfo.getProviderId();
	}

	@Override
	public void setMarketValueDataSource(IMarketValueDataSource marketValueDataSource) {
		this.marketValueDataSource=marketValueDataSource;
	}

	@Override
	public void stop() {
		states.stop();
	}

	@Override
	public void handleStop() {
		publisher.stop();		
	}

	@Override
	public void start() {
		states.start();
	}

	@Override
	public void handleStart() {
		publisher.start();
		// set instrument set
		marketValueDataSource.setInstrumentSet(providerConfigInfo.getInstrumentSet());
		process();
	}

	private void process() {			
		ThreadFactory factory= new ThreadFactory() {			
			@Override
			public Thread newThread(Runnable r) {				
				return new Thread(r, getProviderId()+"-provider-thread");
			}
		};
		
		executorService=Executors.newFixedThreadPool(1,factory);
		Runnable runnable = new Runnable (){

			@Override
			public void run() {
				while (states.isStarted() && marketValueDataSource.hasNext()){
					MarketValue marketValue = marketValueDataSource.next();
					marketValue.setProviderId(getProviderId());
					publisher.publish(marketValue);
					// lets give back to object pool
					// TODO 
					//XXXXXXXX Not working
					
					marketValueDataSource.reset(marketValue);
				}				
			}			
		};
		executorService.execute(runnable);		
	}
		
	@Override
	public String getName() {		
		return "Provider "+ getProviderId();
	}

	public ProviderConfigInfo getProviderConfigInfo() {
		return providerConfigInfo;
	}

	public void setProviderConfigInfo(ProviderConfigInfo providerConfigInfo) {
		this.providerConfigInfo = providerConfigInfo;
	}

	public IPublisher getPublisher() {
		return publisher;
	}

	public void setPublisher(IPublisher publisher) {
		this.publisher = publisher;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getProviderId() == null) ? 0 : getProviderId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Provider other = (Provider) obj;
		if (getProviderId() == null) {
			if (other.getProviderId() != null)
				return false;
		} else if (!getProviderId().equals(other.getProviderId()))
			return false;
		return true;
	}

	@Override
	public void setHandle(IState state) {
		this.states=state;
	}
}
