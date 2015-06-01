package com.blogspot.denizstij.paw.common.serializer;

import com.blogspot.denizstij.paw.common.MarketValue;
/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public interface ISerializer {

	byte [] serialize(MarketValue marketValue) throws Exception;

	MarketValue deserialize(byte[] byteArr) throws Exception;

}
