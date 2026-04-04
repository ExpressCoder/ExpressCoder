package com.parkinglot.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parkinglot.dto.VehicleRequest;
import com.parkinglot.dto.VehicleResponse;
import com.parkinglot.dto.VehicleType;
import com.parkinglot.exception.ParkingException;
import com.parkinglot.service.ParkingLotService;

@RequestMapping("/parkingLot")
@RestController
public class ParkingLotController {
	
	ParkingLotService parkingLotService;

	public ParkingLotController(ParkingLotService parkingLotService) {
		this.parkingLotService = parkingLotService;
	}
	 
	
	@GetMapping("/availableLots")
	public ResponseEntity<Long> totalAvailableLots() {
		return new ResponseEntity<>(parkingLotService.getTotalAvailableLots(),HttpStatus.OK);
	}
	
	@GetMapping("/remainingLots")
	public ResponseEntity<Long> remainingLots(VehicleType vehicleType) throws ParkingException {
		return new ResponseEntity<>(parkingLotService.getRemainingLots(vehicleType),HttpStatus.OK);
	}
	
	@PostMapping("/doParking")
	public ResponseEntity<VehicleResponse> enterToParkingLot(@RequestBody VehicleRequest vehicleRequest) throws ParkingException {
		return new ResponseEntity<VehicleResponse>(parkingLotService.enterToParkingLot(vehicleRequest), HttpStatus.OK);
	}
	
	@GetMapping("/exitParking/{registrationNo}")
	public ResponseEntity<VehicleResponse> exitFromParkingLot(@PathVariable("registrationNo") String registrationNo) throws ParkingException {
		return new ResponseEntity<VehicleResponse>(parkingLotService.exitFromParkingLot(registrationNo), HttpStatus.OK);
	}
	
	@GetMapping("/listOfVehiclesInLot")
	public ResponseEntity<List<VehicleRequest>> getVehiclesFromLot() {
		return new ResponseEntity<>(parkingLotService.getVehiclesFromLot(),HttpStatus.OK);
	}
	
	
}
