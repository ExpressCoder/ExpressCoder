package com.parkinglot.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleResponse {
	
	private VehicleType vehicleType;
	private String registrationNo;
	private LocalDateTime enteredTime;
	private String tokenId;
	private String basementNo;
	
	private Double price;
	private LocalDateTime exitTime;
	

}
