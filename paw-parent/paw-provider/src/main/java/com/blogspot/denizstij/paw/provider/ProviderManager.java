package com.blogspot.denizstij.paw.provider;

import java.util.Set;

/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public class ProviderManager {
	private Set<IProvider> providerSet;

	public Set<IProvider> getProviderSet() {
		return providerSet;
	}

	public void setProviderSet(Set<IProvider> providerSet) {
		this.providerSet = providerSet;
	}

	public void start() {
		for (IProvider provider : providerSet) {
			provider.start();
		}		
	}
	
	public void stop() {
		for (IProvider provider : providerSet) {
			provider.stop();
		}		
	}
}