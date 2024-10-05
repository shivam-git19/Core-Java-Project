package com.thrillio.constant;

public enum KidFriendlyStatus {
	
	
	APPROVED("approved"),
	REJECTED("rejected"),
	UNKNOWN("unknown");
	
	private KidFriendlyStatus(String name) {
		this.name();
	}
	
	private String name;
	public String getName() {
		return name;
	}
}
