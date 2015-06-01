package com.blogspot.denizstij.paw.provider.datasource;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.blogspot.denizstij.paw.common.MarketValue;
import com.blogspot.denizstij.paw.common.datasource.pool.MarketValueFactory;

/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public class RandomMarketValueDataSource implements IMarketValueDataSource {
	private static final Logger logger = LoggerFactory.getLogger(RandomMarketValueDataSource.class);	
	
	private  MarketValueFactory marketValueFactory;
	private Random random = new Random(0); 
	
	@Value("${valueOffset:5.5}")
	private double valueOffset;
	@Value("${valueRangeLength:5}")
	private double valueRangeLength;
	
	private String []insrumentArr;
	 
	@Override
	public boolean hasNext() {
		// Always true
		return true;
	}

	@Override
	public MarketValue next() {
		MarketValue marketValue=null;
		try {
			marketValue = marketValueFactory.create();
			marketValue=build(marketValue);
		} catch (Exception e) {
			logger.error("Can not create object:",e);
		}
		return marketValue;
	}
	
	private MarketValue build(MarketValue marketValue) {
		int instIndex=random.nextInt(insrumentArr.length);		
		marketValue.setName(insrumentArr[instIndex]);
		
		double value = random.nextDouble();		
		// lets put the value between a range (ex: 5.5 to 10.5)
		value=valueOffset+value*valueRangeLength;
		marketValue.setValue(value);
		
		return marketValue;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> getInstrumentSet() {
		return Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(insrumentArr)));
	}

	@Override
	public void setInstrumentSet(Set<String> instrumentSet) {
		insrumentArr =  instrumentSet.toArray(new String[0]);
	}

	public double getValueOffset() {
		return valueOffset;
	}

	public void setValueOffset(double valueOffset) {
		this.valueOffset = valueOffset;
	}

	public double getValueRangeLength() {
		return valueRangeLength;
	}

	public void setValueRangeLength(double valueRangeLength) {
		this.valueRangeLength = valueRangeLength;
	}

	public MarketValueFactory getMarketValueFactory() {
		return marketValueFactory;
	}

	public void setMarketValueFactory(MarketValueFactory marketValueFactory) {
		this.marketValueFactory = marketValueFactory;
	}

	@Override
	public void reset(MarketValue marketValue) {
		marketValueFactory.reset(marketValue);		
	}	
}
