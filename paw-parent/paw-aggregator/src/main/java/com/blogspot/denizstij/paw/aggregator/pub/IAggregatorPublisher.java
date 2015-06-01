package com.blogspot.denizstij.paw.aggregator.pub;

import com.blogspot.denizstij.paw.common.state.Controllable;

/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public interface IAggregatorPublisher extends Controllable{	
	void publish(String data);	
}
