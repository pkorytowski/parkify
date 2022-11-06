package com.parkingsolutions.parkify.service;

import com.parkingsolutions.parkify.repository.ParkingRepository;
import com.parkingsolutions.parkify.repository.ReservationRepository;
import com.parkingsolutions.parkify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class that implements logic for Reservation operations
 */
@Component
public class ReservationService {

    private final ReservationRepository rp;
    private final ParkingRepository pr;
    private final UserRepository ur;
    /**
     * Default amount of time for extending reservation and occupation
     */
    private final int DEFAULT_RESERVATION_EXTEND_TIME = 15;

    @Autowired
    public ReservationService(ReservationRepository rp, ParkingRepository pr, UserRepository ur) {
        this.rp = rp;
        this.pr = pr;
        this.ur  = ur;
    }


}
