package com.parkinglot.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class VehicleRequest {
	
	private VehicleType vehicleType;
	private String registrationNo;
	private LocalDateTime enteredDateTime;
	private Floor floor;

}
