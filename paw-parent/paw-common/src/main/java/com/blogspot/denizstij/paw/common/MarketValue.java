package com.blogspot.denizstij.paw.common;

/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public class MarketValue implements IMarketValue{
	
	private String providerId;
	private double value;
	private String name;

	public MarketValue() {
	}

	
	public MarketValue(String providerId, double value, String name) {		
		this.providerId = providerId;
		this.value = value;
		this.name = name;
	}

	public String getProviderId() {
	
		return providerId;
	}

	public String getName() {		
		return name;
	}

	public double getValue() {		
		return value;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MarketValue [providerId=").append(providerId)
				.append(", value=").append(value).append(", name=")
				.append(name).append("]");
		return builder.toString();
	}
}
