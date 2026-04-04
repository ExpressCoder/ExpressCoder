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
	
	private static int MAX_CAPACITY_PER_BASEMENT_B1 = 150;
	private static int MAX_CAPACITY_PER_BASEMENT_B2 = 175;
	private static int MAX_CAPACITY_PER_BASEMENT_B3 = 200;
	private static int MAX_CAPACITY_PER_BASEMENT_B4 = 225;
	
	private static int carLotCounter;
	private static int bikeLotCounter;
	
	private static int basementCounter;
	
	
	static {
		parkingPriceMap = new LinkedHashMap<>();
		parkingPriceMap.put(1, 50.00);
		parkingPriceMap.put(2, 100.00);
		parkingPriceMap.put(3, 150.00);
		parkingPriceMap.put(4, 200.00);
		parkingPriceMap.put(8, 400.00);
		parkingPriceMap.put(16, 800.00);
		
	}
	
	public static long getRemainingLots(VehicleType vehicleType) throws ParkingException {
		switch(vehicleType.getValue()) {
		case "CAR" :
			if(carLotCounter >= TOTAL_AVAILABLE_CAR_LOTS) {
				throw new ParkingException("Car Parking Full");
			}
			break;

		case "BIKE":
			if(bikeLotCounter >= TOTAL_AVAILABLE_BIKE_LOTS) {
				throw new ParkingException("Bike Parking Full");
			}
			break;
		}
		return incrementLotCounter(vehicleType);
	}
	
	private static long incrementLotCounter(VehicleType vehicleType){
		switch(vehicleType.getValue()) {
		case "CAR":
			return ++carLotCounter;
			
		case "BIKE":
			return ++bikeLotCounter;
		}
		return 0;
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

	public static Double getParkingPrice(long hoursDifference) {
		for(Map.Entry<Integer, Double> pp : parkingPriceMap.entrySet()) {
			if(hoursDifference == pp.getKey()) {
				return pp.getValue();
			}
		}
		return null;
	}
	
	public static String getAvailableBasement() {
		
		
		int maxCountB1 = basementMap.get("B1");
		if(maxCountB1 == MAX_CAPACITY_PER_BASEMENT_B1) {
			int maxCountB2 = basementMap.get("B2");
			if(maxCountB2 == MAX_CAPACITY_PER_BASEMENT_B1) {
				int maxCountB3 = basementMap.get("B3");
			}
		}
		
		for(Map.Entry<String, Integer> availBase : basementMap.entrySet()) {
		
				
				basementCounter = MAX_CAPACITY_PER_BASEMENT_B1;
				
				
				if(basementCounter ==0) {
					
				}
				
				
				

			}
		return null;
	}

private static String getBasementNumber() {
	while(basementCounter == 0) {
		
	}
	return null;
}
	


}
