package com.honydev.hotsquesttracker.properties;

public enum QTProperties {
	TRACKINGRESULTS_PATH("trackingresults.path", "trackingresults");
	
	private final String key;
	private final String defaultValue;

	private QTProperties(String key, String defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	public String getKey() {
		return key;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public String toString() {
		return "key: " + key + ", default value: " + defaultValue;
	}
}
