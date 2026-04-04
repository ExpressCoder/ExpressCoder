package com.parkinglot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ParkingExceptionHandler extends Exception {

	
	@ExceptionHandler(ParkingException.class)
	public ResponseEntity<String> handleParkingException(ParkingException e) {
		return new ResponseEntity<>(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
	}
	
}
