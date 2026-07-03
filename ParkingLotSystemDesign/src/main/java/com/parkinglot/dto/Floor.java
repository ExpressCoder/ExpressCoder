package com.parkinglot.dto;

public enum Floor {
	
	FLOOR_1(1, VehicleType.BIKE, 200),
	FLOOR_2(2, VehicleType.BIKE, 200),
	FLOOR_3(3, VehicleType.CAR, 50),
	FLOOR_4(4, VehicleType.CAR, 50),
	FLOOR_5(5, VehicleType.CAR, 50),
	FLOOR_6(6, VehicleType.CAR, 50),
	FLOOR_7(7, VehicleType.CAR, 50);
	
	private int floorNumber;
	private VehicleType vehicleType;
	private int maxCapacity;
	private int currentOccupancy;
	
	Floor(int floorNumber, VehicleType vehicleType, int maxCapacity) {
		this.floorNumber = floorNumber;
		this.vehicleType = vehicleType;
		this.maxCapacity = maxCapacity;
		this.currentOccupancy = 0;
	}
	
	public int getFloorNumber() {
		return floorNumber;
	}
	
	public VehicleType getVehicleType() {
		return vehicleType;
	}
	
	public int getMaxCapacity() {
		return maxCapacity;
	}
	
	public int getCurrentOccupancy() {
		return currentOccupancy;
	}
	
	public int getAvailableCapacity() {
		return maxCapacity - currentOccupancy;
	}
	
	public boolean hasAvailableSpace() {
		return currentOccupancy < maxCapacity;
	}
	
	public void incrementOccupancy() {
		if (currentOccupancy < maxCapacity) {
			currentOccupancy++;
		}
	}
	
	public void decrementOccupancy() {
		if (currentOccupancy > 0) {
			currentOccupancy--;
		}
	}
	
	public static Floor getAvailableFloor(VehicleType vehicleType) {
		for (Floor floor : values()) {
			if (floor.getVehicleType() == vehicleType && floor.hasAvailableSpace()) {
				return floor;
			}
		}
		return null;
	}
	
	public static Floor getFloorByNumber(int floorNumber) {
		for (Floor floor : values()) {
			if (floor.getFloorNumber() == floorNumber) {
				return floor;
			}
		}
		return null;
	}
}
