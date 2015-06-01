package com.blogspot.denizstij.paw.aggregator;

import java.util.Collection;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blogspot.denizstij.paw.common.MarketValue;
import com.blogspot.denizstij.paw.common.math.MathUtil;
import com.google.common.util.concurrent.AtomicDouble;

/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public class AggregationBean {	
	private static final Logger logger = LoggerFactory.getLogger(Aggregator.class);
	private final String instrumentName;
	private volatile double aggValue=0;
	private HashMap <String, AtomicDouble> providerValMap = new HashMap<>();
	
	public AggregationBean(String instrumentName){
		this.instrumentName=instrumentName;
	}
		
	public double getAggValue(){
		return aggValue;
	}

	// Note, this method is not thread safe, as this method would be called only from initialization/startup of aggregator
	public void addProvider(String providerId) {		
		providerValMap.put(providerId, new AtomicDouble(0));		
	}
	
	public void updateValue(MarketValue event) {			
		AtomicDouble curProviderValAtomic = providerValMap.get(event.getProviderId());		
		if (curProviderValAtomic==null){
			logger.error("Unknown provider:"+event.getProviderId());
			return ;
		}
		curProviderValAtomic.set(event.getValue());
	}
		
	public boolean isAggregationStateChanged() {
		Collection<AtomicDouble> values = providerValMap.values();
		if (values.isEmpty()) {
			return false;
		}

		double total = 0;
		int totalProvider = 0;
		for (AtomicDouble atomicDouble : values) {
			totalProvider++;
			total += atomicDouble.get();
		}

		double newAggValue = total / totalProvider;

		if (MathUtil.isEqual(newAggValue, aggValue)) {
			// nothing to publish as value has not changed
			return false;
		} else {
			aggValue = newAggValue;
			return true;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AggregationBean [instrumentName=")
				.append(instrumentName).append(", aggValue=").append(aggValue)
				.append(", providerValuesMap=")
				.append(providerValMap).append("]");
		return builder.toString();
	}
}
