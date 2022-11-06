package com.parkingsolutions.parkify.controller;

import com.parkingsolutions.parkify.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller for CRUD on Parking instances
 */
@RestController
@RequestMapping("parking")
public class ParkingController {
    private final ParkingService ps;

    @Autowired
    public ParkingController(ParkingService ps) {
        this.ps = ps;
    }


}
