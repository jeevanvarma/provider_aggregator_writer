package com.blogspot.denizstij.paw.common.datasource.pool;

import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.pool2.impl.GenericObjectPool;

import com.blogspot.denizstij.paw.common.MarketValue;

/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public class MarketValueFactory  {	
	private Executor executorService= Executors.newCachedThreadPool(); 
	private GenericObjectPool<MarketValue> pool;
	
	public MarketValue create() throws Exception {
		return pool.borrowObject();
	}
	
	public void reset(MarketValue marketValue)  {
		pool.returnObject(marketValue);
	}

	public GenericObjectPool<MarketValue> getPool() {
		return pool;
	}

	public void setPool(GenericObjectPool<MarketValue> pool) {
		this.pool = pool;
	}

	public void asychReset(final Collection<MarketValue> allMarketValSets) {
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				for (MarketValue marketValue : allMarketValSets) {
					reset(marketValue);
				}								
			}
		});		
	}
}
