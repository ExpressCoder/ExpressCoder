package com.parkinglot.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.parkinglot.dto.Floor;
import com.parkinglot.dto.VehicleRequest;
import com.parkinglot.dto.VehicleResponse;
import com.parkinglot.dto.VehicleType;
import com.parkinglot.exception.ParkingException;
import com.parkinglot.util.ParkingLotUtil;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
	
	private static Map<String, VehicleRequest> vehicleDetails = new HashMap<>();
	
	public ParkingLotServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public long getTotalAvailableLots() {
		return ParkingLotUtil.getTotalAvailableLots();
	}

	@Override
	public long getRemainingLots(VehicleType vehicleType) throws ParkingException {
		return ParkingLotUtil.getRemainingLots(vehicleType);
	}

	@Override
	public VehicleResponse enterToParkingLot(VehicleRequest vehicleRequest) throws ParkingException {
		if(vehicleDetails.get(vehicleRequest.getRegistrationNo()) != null)
			throw new ParkingException("Vehicle Already in Parking Lot:"+vehicleRequest.getRegistrationNo());
		
		Floor availableFloor = ParkingLotUtil.getAvailableFloor(vehicleRequest.getVehicleType());
		
		if(availableFloor != null) {
			LocalDateTime enteredTime = LocalDateTime.now();
			vehicleRequest.setEnteredDateTime(enteredTime);
			vehicleRequest.setFloor(availableFloor);
			vehicleDetails.put(vehicleRequest.getRegistrationNo(), vehicleRequest);
			
			ParkingLotUtil.incrementFloorOccupancy(availableFloor);
			
			VehicleResponse vehicleResponse = new VehicleResponse();
			vehicleResponse.setEnteredTime(vehicleRequest.getEnteredDateTime());
			vehicleResponse.setVehicleType(vehicleRequest.getVehicleType());
			vehicleResponse.setRegistrationNo(vehicleRequest.getRegistrationNo());
			vehicleResponse.setTokenId(UUID.randomUUID().toString());
			vehicleResponse.setBasementNo("Vehicle Parked in Floor " + availableFloor.getFloorNumber());
			return vehicleResponse;
		}
		return null;
	}

	@Override
	public VehicleResponse exitFromParkingLot(String registrationNo) throws ParkingException {
		VehicleResponse vehicleResponse =null;
		if(vehicleDetails.get(registrationNo) != null) {
			VehicleRequest vehicleRequest = vehicleDetails.get(registrationNo);

			LocalDateTime exitTime = LocalDateTime.now();
			long hoursDifference = ChronoUnit.HOURS.between(exitTime, vehicleRequest.getEnteredDateTime());
			hoursDifference = 1;
			Double price = ParkingLotUtil.getParkingPrice(hoursDifference);
			
			ParkingLotUtil.decrementFloorOccupancy(vehicleRequest.getFloor());
			
			vehicleResponse = new VehicleResponse();
			vehicleResponse.setEnteredTime(vehicleRequest.getEnteredDateTime());
			vehicleResponse.setVehicleType(vehicleRequest.getVehicleType());
			vehicleResponse.setRegistrationNo(vehicleRequest.getRegistrationNo());
			vehicleResponse.setExitTime(exitTime);
			vehicleResponse.setPrice(price);
			vehicleDetails.remove(registrationNo);
		} else {
			throw new ParkingException("Registration Details Not Found:"+registrationNo);
		}
		return vehicleResponse;

	}

	@Override
	public List<VehicleRequest> getVehiclesFromLot() {
		return vehicleDetails.entrySet().stream().map(e->e.getValue()).collect(Collectors.toList());
	}
	
	@Override
	public long getAvailableCapacityByFloor(int floorNumber) {
		return ParkingLotUtil.getAvailableCapacityByFloor(floorNumber);
	}
	
	@Override
	public long getCurrentOccupancyByFloor(int floorNumber) {
		return ParkingLotUtil.getCurrentOccupancyByFloor(floorNumber);
	}

}
