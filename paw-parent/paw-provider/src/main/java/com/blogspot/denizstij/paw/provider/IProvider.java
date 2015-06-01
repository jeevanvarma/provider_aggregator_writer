package com.blogspot.denizstij.paw.provider;

import com.blogspot.denizstij.paw.common.state.Controllable;
import com.blogspot.denizstij.paw.provider.datasource.IMarketValueDataSource;
/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public interface IProvider extends Controllable{
	String getProviderId();
	void setMarketValueDataSource(IMarketValueDataSource marketValueDataSource);	
}
