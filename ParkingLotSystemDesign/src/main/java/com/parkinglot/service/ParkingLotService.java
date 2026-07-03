package com.parkinglot.service;

import java.util.List;

import com.parkinglot.dto.VehicleRequest;
import com.parkinglot.dto.VehicleResponse;
import com.parkinglot.dto.VehicleType;
import com.parkinglot.exception.ParkingException;

public interface ParkingLotService {

	long getTotalAvailableLots();

	VehicleResponse enterToParkingLot(VehicleRequest vehicleRequest) throws ParkingException;

	VehicleResponse exitFromParkingLot(String registrationNo) throws ParkingException;

	long getRemainingLots(VehicleType vehicleType) throws ParkingException;
	
	List<VehicleRequest> getVehiclesFromLot();
	
	long getAvailableCapacityByFloor(int floorNumber);
	
	long getCurrentOccupancyByFloor(int floorNumber);

}
