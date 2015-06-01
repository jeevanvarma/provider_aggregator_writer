package com.blogspot.denizstij.paw.common.datasource.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.blogspot.denizstij.paw.common.MarketValue;

/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
class MarketValuePooledFactory extends BasePooledObjectFactory<MarketValue> {
	
	@Override
	public MarketValue create() throws Exception {
		return new MarketValue();
	}

	@Override
	public PooledObject<MarketValue> wrap(MarketValue marketValue) {		
		return new DefaultPooledObject<MarketValue>(marketValue);
	}
}
