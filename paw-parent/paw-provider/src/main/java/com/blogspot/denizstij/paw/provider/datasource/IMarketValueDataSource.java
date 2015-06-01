package com.blogspot.denizstij.paw.provider.datasource;

import java.util.Iterator;
import java.util.Set;

import com.blogspot.denizstij.paw.common.MarketValue;

/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public interface IMarketValueDataSource extends Iterator<MarketValue> {
	Set<String> getInstrumentSet();
	void setInstrumentSet(Set<String> instrumentSet);
	public void reset(MarketValue marketValue) ;
}
