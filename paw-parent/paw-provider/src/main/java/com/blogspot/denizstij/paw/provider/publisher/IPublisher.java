package com.blogspot.denizstij.paw.provider.publisher;

import com.blogspot.denizstij.paw.common.MarketValue;
import com.blogspot.denizstij.paw.common.state.Controllable;

/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public interface IPublisher extends Controllable {	
	void publish(MarketValue marketValue);
}
