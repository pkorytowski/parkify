package com.parkingsolutions.parkify.service;

import com.parkingsolutions.parkify.document.Parking;
import com.parkingsolutions.parkify.document.Reservation;
import com.parkingsolutions.parkify.repository.ParkingRepository;
import com.parkingsolutions.parkify.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class ReservationService {
    private final ReservationRepository rp;
    private final ParkingRepository pr;

    @Autowired
    public ReservationService(ReservationRepository rp, ParkingRepository pr) {
        this.rp = rp;
        this.pr = pr;
    }

    public List<Reservation> getAll() {
        return rp.findAll();
    }

    public List<Reservation> getAllByUserId(String id) {
        return rp.findAllByUserId(id);
    }

    public List<Reservation> getAllByParkingId(String id) {
        return rp.findAllByParkingId(id);
    }

    public Reservation getOneById(String id) {
        return rp.findFirstById(id);
    }

    @Transactional
    public Reservation save(Reservation reservation) {
        Parking parking = pr.findFirstById(reservation.getParkingId());
        boolean result = parking.reserveSpot(reservation.getLaneName());
        if (!result) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot reserve this spot");
        }
        pr.save(parking);
        return rp.save(reservation);
    }

    @Transactional
    public Reservation updateReservation(Reservation reservation) {
        Reservation reservationOld = rp.findFirstById(reservation.getId());
        if (!reservationOld.getParkingId().equals(reservation.getParkingId())) {
            Parking result = changeParking(reservationOld, reservation);
            if (result == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot change parking");
            }
        }
        if (!reservationOld.getReservationStatus().equals(reservation.getReservationStatus())) {

        }
    }

    //todo return pair of parkings
    private Parking changeParking(Reservation reservationOld, Reservation reservationNew) {
        Parking parkingNew = pr.findFirstById(reservationNew.getParkingId());
        Parking parkingOld = pr.findFirstById(reservationOld.getParkingId());
        boolean result1 = parkingNew.reserveSpot(reservationNew.getLaneName());
        boolean result2 = parkingOld.freeSpot(reservationOld.getLaneName());
        if (!result1 || !result2) {
            return null;
        }
        return parkingNew;
    }






}
