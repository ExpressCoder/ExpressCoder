package com.parkinglot.service;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkinglot.dto.VehicleRequest;
import com.parkinglot.dto.VehicleResponse;
import com.parkinglot.dto.VehicleType;
import com.parkinglot.dto.Floor;

public class ParkingLotServiceImplTest {

    @BeforeEach
    void resetState() throws Exception {
        // reset Floor occupancies
        Field f = Floor.class.getDeclaredField("currentOccupancy");
        f.setAccessible(true);
        for (Floor floor : Floor.values()) {
            f.setInt(floor, 0);
        }

        // clear vehicleDetails map in service
        Field vd = ParkingLotServiceImpl.class.getDeclaredField("vehicleDetails");
        vd.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, VehicleRequest> map = (Map<String, VehicleRequest>) vd.get(null);
        map.clear();
    }

    @Test
    void testEnterAndExit() throws Exception {
        ParkingLotServiceImpl service = new ParkingLotServiceImpl();

        VehicleRequest req = new VehicleRequest();
        req.setRegistrationNo("TEST-123");
        req.setVehicleType(VehicleType.CAR);
        req.setEnteredDateTime(LocalDateTime.now());

        VehicleResponse enterResp = service.enterToParkingLot(req);
        assertNotNull(enterResp);
        assertEquals("TEST-123", enterResp.getRegistrationNo());
        assertEquals(VehicleType.CAR, enterResp.getVehicleType());
        assertNotNull(enterResp.getFloor());

        // occupancy should have increased on that floor
        Floor floor = enterResp.getFloor();
        assertEquals(1, floor.getCurrentOccupancy());

        // exit
        VehicleResponse exitResp = service.exitFromParkingLot("TEST-123");
        assertNotNull(exitResp);
        assertEquals("TEST-123", exitResp.getRegistrationNo());
        assertNotNull(exitResp.getExitTime());
        assertEquals(50.0, exitResp.getPrice());

        // after exit, occupancy goes back to 0
        assertEquals(0, floor.getCurrentOccupancy());
    }
}
