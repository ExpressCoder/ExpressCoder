package com.parkinglot.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.parkinglot.dto.VehicleType;

@Entity
@Table(name = "vehicles")
public class VehicleEntity {

    @Id
    @Column(name = "registration_no", nullable = false)
    private String registrationNo;

    @Column(name = "vehicle_type")
    private VehicleType vehicleType;

    @Column(name = "entered_time")
    private LocalDateTime enteredDateTime;

    @Column(name = "floor_number")
    private Integer floorNumber;

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public LocalDateTime getEnteredDateTime() {
        return enteredDateTime;
    }

    public void setEnteredDateTime(LocalDateTime enteredDateTime) {
        this.enteredDateTime = enteredDateTime;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }
}
