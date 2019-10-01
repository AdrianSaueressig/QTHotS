package com.honydev.hotsquesttracker.error;

import javafx.scene.control.Alert.AlertType;

public enum AlertMessage {
	
	NO_HERO_SELECTED(0, "No Hero selected", "You must enter a valid Heroname!", AlertType.ERROR),
	NO_TALENTS_SELECTED(1, "No Talents selected", "You must enter a valid talent for your hero!", AlertType.ERROR),
	NO_MAP_SELECTED(2, "No map selected", "You must enter a valid map!", AlertType.ERROR);
	
	private final int code;
	private final String shortText;
	private final String longText;
	private final AlertType alertType;

	private AlertMessage(int code, String shortText, String longText, AlertType alertType) {
		this.code = code;
		this.shortText = shortText;
		this.longText = longText;
		this.alertType = alertType;
	}

	public int getCode() {
		return code;
	}

	public String getShortText() {
		return shortText;
	}

	public String getLongText() {
		return longText;
	}

	public AlertType getAlertType() {
		return alertType;
	}

	@Override
	public String toString() {
		return code + ": " + shortText + ", " + longText;
	}
}
