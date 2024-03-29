package com.parkingsolutions.parkify.bean;

import com.parkingsolutions.parkify.document.Parking;
import com.parkingsolutions.parkify.document.Reservation;

/**
 * Class that allows return full info about reservation and parking details in one get request
 */
public class ReservationFull {
    Reservation reservation;
    Parking parking;

    public ReservationFull(Reservation reservation, Parking parking) {
        this.reservation = reservation;
        this.parking = parking;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Parking getParking() {
        return parking;
    }

    public void setParking(Parking parking) {
        this.parking = parking;
    }
}
