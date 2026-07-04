package com.parkinglot.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parkinglot.dto.Floor;
import com.parkinglot.dto.VehicleRequest;
import com.parkinglot.dto.VehicleResponse;
import com.parkinglot.dto.VehicleType;
import com.parkinglot.entity.VehicleEntity;
import com.parkinglot.exception.ParkingException;
import com.parkinglot.repository.VehicleRepository;
import com.parkinglot.util.ParkingLotUtil;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public ParkingLotServiceImpl() {
        // default constructor
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
    @Transactional
    public VehicleResponse enterToParkingLot(VehicleRequest vehicleRequest) throws ParkingException {
        if (vehicleRepository.existsById(vehicleRequest.getRegistrationNo()))
            throw new ParkingException("Vehicle Already in Parking Lot:" + vehicleRequest.getRegistrationNo());

        Floor availableFloor = ParkingLotUtil.getAvailableFloor(vehicleRequest.getVehicleType());

        if (availableFloor != null) {
            LocalDateTime enteredTime = LocalDateTime.now();
            vehicleRequest.setEnteredDateTime(enteredTime);
            vehicleRequest.setFloor(availableFloor);

            // persist vehicle to DB
            VehicleEntity e = new VehicleEntity();
            e.setRegistrationNo(vehicleRequest.getRegistrationNo());
            e.setVehicleType(vehicleRequest.getVehicleType());
            e.setEnteredDateTime(vehicleRequest.getEnteredDateTime());
            e.setFloorNumber(availableFloor.getFloorNumber());
            vehicleRepository.save(e);

            ParkingLotUtil.incrementFloorOccupancy(availableFloor);

            VehicleResponse vehicleResponse = new VehicleResponse();
            vehicleResponse.setEnteredTime(vehicleRequest.getEnteredDateTime());
            vehicleResponse.setVehicleType(vehicleRequest.getVehicleType());
            vehicleResponse.setRegistrationNo(vehicleRequest.getRegistrationNo());
            vehicleResponse.setTokenId(UUID.randomUUID().toString());
            vehicleResponse.setBasementNo("Vehicle Parked in Floor " + availableFloor.getFloorNumber());
            vehicleResponse.setFloor(availableFloor);
            return vehicleResponse;
        }
        return null;
    }

    @Override
    public List<VehicleRequest> getVehiclesFromLot() {
        return vehicleRepository.findAll().stream().map(e -> {
            VehicleRequest r = new VehicleRequest();
            r.setRegistrationNo(e.getRegistrationNo());
            r.setVehicleType(e.getVehicleType());
            r.setEnteredDateTime(e.getEnteredDateTime());
            r.setFloor(Floor.getFloorByNumber(e.getFloorNumber()));
            return r;
        }).collect(Collectors.toList());
    }

    @Override
    public long getAvailableCapacityByFloor(int floorNumber) {
        return ParkingLotUtil.getAvailableCapacityByFloor(floorNumber);
    }

    @Override
    public long getCurrentOccupancyByFloor(int floorNumber) {
        return ParkingLotUtil.getCurrentOccupancyByFloor(floorNumber);
    }

    @Override
    @Transactional
    public VehicleResponse exitFromParkingLot(String registrationNo) throws ParkingException {
        VehicleResponse vehicleResponse = null;
        var opt = vehicleRepository.findById(registrationNo);
        if (opt.isPresent()) {
            VehicleEntity vehicle = opt.get();
            LocalDateTime exitTime = LocalDateTime.now();
            long hoursDifference = ChronoUnit.HOURS.between(vehicle.getEnteredDateTime(), exitTime);
            if (hoursDifference <= 0)
                hoursDifference = 1;
            Double price = ParkingLotUtil.getParkingPrice(hoursDifference);
            Floor floor = Floor.getFloorByNumber(vehicle.getFloorNumber());
            ParkingLotUtil.decrementFloorOccupancy(floor);
            vehicleResponse = new VehicleResponse();
            vehicleResponse.setEnteredTime(vehicle.getEnteredDateTime());
            vehicleResponse.setVehicleType(vehicle.getVehicleType());
            vehicleResponse.setRegistrationNo(vehicle.getRegistrationNo());
            vehicleResponse.setExitTime(exitTime);
            vehicleResponse.setPrice(price);
            vehicleRepository.deleteById(registrationNo);
        } else {
            throw new ParkingException("Registration Details Not Found:" + registrationNo);
        }
        return vehicleResponse;
    }

}
