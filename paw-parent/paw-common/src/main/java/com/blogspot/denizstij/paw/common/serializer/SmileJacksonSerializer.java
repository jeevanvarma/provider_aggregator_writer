package com.blogspot.denizstij.paw.common.serializer;

import com.blogspot.denizstij.paw.common.MarketValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public class SmileJacksonSerializer implements ISerializer {
	SmileFactory f = new SmileFactory();
	ObjectMapper mapper = new ObjectMapper(f);
	
	@Override
	public byte[] serialize(MarketValue marketValue) throws Exception {
		byte bytes[]=mapper.writeValueAsBytes(marketValue);
		return bytes;
	}

	@Override
	public MarketValue deserialize(byte[] byteArr) throws Exception {
		MarketValue marketValue=mapper.readValue(byteArr, 0, byteArr.length, MarketValue.class);
		return marketValue;
	}
}
