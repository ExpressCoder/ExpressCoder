package com.parkinglot.util;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkinglot.dto.Floor;

public class ParkingLotUtilTest {

    @BeforeEach
    void resetFloors() throws Exception {
        Field f = Floor.class.getDeclaredField("currentOccupancy");
        f.setAccessible(true);
        for (Floor floor : Floor.values()) {
            f.setInt(floor, 0);
        }
    }

    @Test
    void testGetParkingPrice() {
        Double p = ParkingLotUtil.getParkingPrice(1);
        assertNotNull(p);
        assertEquals(50.00, p);

        assertNull(ParkingLotUtil.getParkingPrice(999));
    }

    @Test
    void testCapacities() throws Exception {
        long total = ParkingLotUtil.getTotalAvailableLots();
        // 2 bike floors * 200 + 5 car floors * 50 = 400 + 250 = 650
        assertEquals(650L, total);

        long bikes = ParkingLotUtil.getRemainingLots(com.parkinglot.dto.VehicleType.BIKE);
        assertEquals(400L, bikes);

        long cars = ParkingLotUtil.getRemainingLots(com.parkinglot.dto.VehicleType.CAR);
        assertEquals(250L, cars);

        Floor availBike = ParkingLotUtil.getAvailableFloor(com.parkinglot.dto.VehicleType.BIKE);
        assertNotNull(availBike);
        assertEquals(com.parkinglot.dto.VehicleType.BIKE, availBike.getVehicleType());

        // increment occupancy and verify counts change
        ParkingLotUtil.incrementFloorOccupancy(availBike);
        assertEquals(availBike.getCurrentOccupancy(), 1);
        assertEquals(availBike.getAvailableCapacity(), availBike.getMaxCapacity() - 1);

        ParkingLotUtil.decrementFloorOccupancy(availBike);
        assertEquals(availBike.getCurrentOccupancy(), 0);
    }
}
