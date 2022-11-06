package com.parkingsolutions.parkify.service;

import com.parkingsolutions.parkify.repository.ParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class that implements logic for Parking operations
 */
@Component
public class ParkingService {

    private final ParkingRepository pr;

    @Autowired
    public ParkingService(ParkingRepository pr) {
        this.pr = pr;
    }

}
