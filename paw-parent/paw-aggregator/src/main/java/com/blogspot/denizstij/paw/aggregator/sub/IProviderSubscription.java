package com.blogspot.denizstij.paw.aggregator.sub;

import com.blogspot.denizstij.paw.aggregator.IAggregator;
import com.blogspot.denizstij.paw.common.ProviderConfigInfo;
/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public interface IProviderSubscription {

	void subscribe(IAggregator aggregator, ProviderConfigInfo providerConfig);

	void unsubscribe(IAggregator aggregator, ProviderConfigInfo providerConfig);
}
