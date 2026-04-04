package com.parkinglot.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.parkinglot.dto.VehicleType;
import com.parkinglot.exception.ParkingException;

public class ParkingLotUtil {
	
	public static final long TOTAL_AVAILABLE_LOTS = 3;
	
	public static final long TOTAL_AVAILABLE_CAR_LOTS = 1;
	public static final long TOTAL_AVAILABLE_BIKE_LOTS = 2;
	
	private static Map<Integer,Double> parkingPriceMap;
	private static Map<String,Integer> basementMap = new HashMap<>();
	
	private static final int MAX_CAPACITY_PER_BASEMENT_B1 = 150;
	private static final int MAX_CAPACITY_PER_BASEMENT_B2 = 175;
	private static final int MAX_CAPACITY_PER_BASEMENT_B3 = 200;
	private static final int MAX_CAPACITY_PER_BASEMENT_B4 = 225;
	
	private static final String[] BASEMENTS = {"B1", "B2", "B3", "B4"};
	private static final int[] BASEMENT_CAPACITIES = {
		MAX_CAPACITY_PER_BASEMENT_B1,
		MAX_CAPACITY_PER_BASEMENT_B2,
		MAX_CAPACITY_PER_BASEMENT_B3,
		MAX_CAPACITY_PER_BASEMENT_B4
	};
	
	private static int carLotCounter;
	private static int bikeLotCounter;
	
	
	static {
		parkingPriceMap = new LinkedHashMap<>();
		parkingPriceMap.put(1, 50.00);
		parkingPriceMap.put(2, 100.00);
		parkingPriceMap.put(3, 150.00);
		parkingPriceMap.put(4, 200.00);
		parkingPriceMap.put(8, 400.00);
		parkingPriceMap.put(16, 800.00);
		
		for (String basement : BASEMENTS) {
			basementMap.put(basement, 0);
		}
	}
	
	public static long getRemainingLots(VehicleType vehicleType) throws ParkingException {
		switch(vehicleType.getValue()) {
		case "CAR":
			if(carLotCounter >= TOTAL_AVAILABLE_CAR_LOTS) {
				throw new ParkingException("Car Parking Full");
			}
			return TOTAL_AVAILABLE_CAR_LOTS - carLotCounter;

		case "BIKE":
			if(bikeLotCounter >= TOTAL_AVAILABLE_BIKE_LOTS) {
				throw new ParkingException("Bike Parking Full");
			}
			return TOTAL_AVAILABLE_BIKE_LOTS - bikeLotCounter;

		default:
			throw new ParkingException("Unknown vehicle type: " + vehicleType.getValue());
		}
	}
	
	public static void incrementLotCounter(VehicleType vehicleType) {
		switch(vehicleType.getValue()) {
		case "CAR":
			carLotCounter++;
			break;
		case "BIKE":
			bikeLotCounter++;
			break;
		}
	}
	
	public static long decrementLotCounter(VehicleType vehicleType) throws ParkingException {
		switch(vehicleType.getValue()) {
		case "CAR":
			return --carLotCounter;
			
		case "BIKE":
			return --bikeLotCounter;
		}
		return 0;
	}

	public static double getParkingPrice(long hoursDifference) {
		if (hoursDifference <= 0) {
			hoursDifference = 1;
		}
		Double price = null;
		for (Map.Entry<Integer, Double> pp : parkingPriceMap.entrySet()) {
			if (hoursDifference <= pp.getKey()) {
				return pp.getValue();
			}
			price = pp.getValue();
		}
		return price;
	}
	
	public static String getAvailableBasement() {
		for (int i = 0; i < BASEMENTS.length; i++) {
			String basement = BASEMENTS[i];
			int currentCount = basementMap.getOrDefault(basement, 0);
			if (currentCount < BASEMENT_CAPACITIES[i]) {
				basementMap.put(basement, currentCount + 1);
				return basement;
			}
		}
		return "No Basement Available";
	}
	


}
