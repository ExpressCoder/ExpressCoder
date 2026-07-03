package com.parkinglot.util;

import java.util.LinkedHashMap;
import java.util.Map;

import com.parkinglot.dto.Floor;
import com.parkinglot.dto.VehicleType;
import com.parkinglot.exception.ParkingException;

public class ParkingLotUtil {
	
	public static final int TOTAL_FLOORS = 7;
	public static final int BIKE_FLOORS = 2;
	public static final int CAR_FLOORS = 5;
	public static final int MAX_BIKE_CAPACITY_PER_FLOOR = 200;
	public static final int MAX_CAR_CAPACITY_PER_FLOOR = 50;
	
	private static Map<Integer,Double> parkingPriceMap;
	
	static {
		parkingPriceMap = new LinkedHashMap<>();
		parkingPriceMap.put(1, 50.00);
		parkingPriceMap.put(2, 100.00);
		parkingPriceMap.put(3, 150.00);
		parkingPriceMap.put(4, 200.00);
		parkingPriceMap.put(8, 400.00);
		parkingPriceMap.put(16, 800.00);
	}
	
	public static long getTotalAvailableLots() {
		long total = 0;
		for (Floor floor : Floor.values()) {
			total += floor.getAvailableCapacity();
		}
		return total;
	}
	
	public static long getRemainingLots(VehicleType vehicleType) throws ParkingException {
		long total = 0;
		for (Floor floor : Floor.values()) {
			if (floor.getVehicleType() == vehicleType) {
				total += floor.getAvailableCapacity();
			}
		}
		
		if (total == 0) {
			throw new ParkingException(vehicleType.getValue() + " Parking Full");
		}
		
		return total;
	}
	
	public static Floor getAvailableFloor(VehicleType vehicleType) throws ParkingException {
		Floor floor = Floor.getAvailableFloor(vehicleType);
		if (floor == null) {
			throw new ParkingException(vehicleType.getValue() + " Parking Full - No available floors");
		}
		return floor;
	}
	
	public static void incrementFloorOccupancy(Floor floor) {
		floor.incrementOccupancy();
	}
	
	public static void decrementFloorOccupancy(Floor floor) {
		floor.decrementOccupancy();
	}

	public static Double getParkingPrice(long hoursDifference) {
		for(Map.Entry<Integer, Double> pp : parkingPriceMap.entrySet()) {
			if(hoursDifference == pp.getKey()) {
				return pp.getValue();
			}
		}
		return null;
	}
	
	public static long getAvailableCapacityByFloor(int floorNumber) {
		Floor floor = Floor.getFloorByNumber(floorNumber);
		return floor != null ? floor.getAvailableCapacity() : 0;
	}
	
	public static long getCurrentOccupancyByFloor(int floorNumber) {
		Floor floor = Floor.getFloorByNumber(floorNumber);
		return floor != null ? floor.getCurrentOccupancy() : 0;
	}
}
