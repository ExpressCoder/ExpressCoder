package com.parkinglot.dto;

public enum VehicleType {

	CAR("CAR"),BIKE("BIKE");

	private String value;
	
	VehicleType(String value) {
	  this.value = value;	
	}
	
	public String getValue() {
		return value;
	}

}
