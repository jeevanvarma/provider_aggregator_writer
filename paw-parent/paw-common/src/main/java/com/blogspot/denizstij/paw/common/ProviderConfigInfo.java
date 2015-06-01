package com.blogspot.denizstij.paw.common;

import java.util.Collections;
import java.util.Set;

/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public class ProviderConfigInfo {
	private String providerId;	
	private String connectionUrl;	
	private Set<String> instrumentSet;	
	private String topic;
	
	public String getProviderId() {
		return providerId;
	}
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	public String getConnectionUrl() {
		return connectionUrl;
	}
	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}
	public String getTopic() {
		return topic;
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public Set<String> getInstrumentSet() {
		return Collections.unmodifiableSet(instrumentSet);
	}
	public void setInstrumentSet(Set<String> instrumentSet) {
		this.instrumentSet = instrumentSet;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(256);
		builder.append("ProviderConfigInfo [providerId=").append(providerId)
				.append(", connectionUrl=").append(connectionUrl)
				.append(", instrumentSet=").append(instrumentSet)
				.append(", topic=").append(topic).append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((providerId == null) ? 0 : providerId.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProviderConfigInfo other = (ProviderConfigInfo) obj;
		if (providerId == null) {
			if (other.providerId != null)
				return false;
		} else if (!providerId.equals(other.providerId))
			return false;
		return true;
	}	
	
}
