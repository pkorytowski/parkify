package com.parkingsolutions.parkify.service;

import com.parkingsolutions.parkify.bean.ReservationFull;
import com.parkingsolutions.parkify.common.ReservationStatus;
import com.parkingsolutions.parkify.document.Parking;
import com.parkingsolutions.parkify.document.Reservation;
import com.parkingsolutions.parkify.repository.ParkingRepository;
import com.parkingsolutions.parkify.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<Reservation> getAllByUserIdAndReservationStatusEquals(String id, ReservationStatus reservationStatus) {
        return rp.findAllByUserIdAndReservationStatusEquals(id, reservationStatus);
    }

    public List<Reservation> getAllByParkingId(String id) {
        return rp.findAllByParkingId(id);
    }

    public List<ReservationFull> getFullReservationsByUserId(String id) {
        List<Reservation> reservations = getAllByUserId(id);
        List<ReservationFull> reservationFullList = new ArrayList<>();
        for (Reservation reservation: reservations) {
            Parking tmpParking = pr.findOneById(reservation.getParkingId());
            reservationFullList.add(new ReservationFull(reservation, tmpParking));
        }
        return reservationFullList;
    }

    public List<ReservationFull> getFullReservationsByUserIdAndReservationStatusEqualsReserved(String id) {
        List<Reservation> reservations = getAllByUserIdAndReservationStatusEquals(id, ReservationStatus.RESERVED);
        List<ReservationFull> reservationFullList = new ArrayList<>();
        for (Reservation reservation: reservations) {
            Parking tmpParking = pr.findOneById(reservation.getParkingId());
            reservationFullList.add(new ReservationFull(reservation, tmpParking));
        }
        return reservationFullList;
    }

    public List<ReservationFull> getFullReservationsByUserIdAndReservationStatusEqualsOccupied(String id) {
        List<Reservation> reservations = getAllByUserIdAndReservationStatusEquals(id, ReservationStatus.OCCUPIED);
        List<ReservationFull> reservationFullList = new ArrayList<>();
        for (Reservation reservation: reservations) {
            Parking tmpParking = pr.findOneById(reservation.getParkingId());
            reservationFullList.add(new ReservationFull(reservation, tmpParking));
        }
        return reservationFullList;
    }

    public List<ReservationFull> getActiveFullReservationsByUserId(String id) {
        List<ReservationFull> reservedReservations = getFullReservationsByUserIdAndReservationStatusEqualsReserved(id);
        List<ReservationFull> occupiedReservations = getFullReservationsByUserIdAndReservationStatusEqualsOccupied(id);
        return Stream.of(reservedReservations, occupiedReservations)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public Reservation getOneById(String id) {
        return rp.findFirstById(id);
    }

    @Transactional
    public Reservation save(Reservation reservation) {
        System.out.println(reservation.getParkingId());
        Parking parking = pr.findOneById(reservation.getParkingId());
        if (reservation.getReservationStatus() == null) {
            reservation.setReservationStatus(ReservationStatus.RESERVED);
            reservation.setReservationStart(LocalDateTime.now());
        }
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
            try {
                reservationOld = changeParking(reservationOld, reservation);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot change parking");
            }
        } else if (reservationOld.getParkingId().equals(reservation.getParkingId()) &&
            !reservationOld.getLaneName().equals(reservation.getLaneName())) {
            try {
                reservationOld = changeLane(reservationOld, reservation);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot change lane on the spot");
            }
        }

        if (!reservationOld.getReservationStatus().equals(reservation.getReservationStatus())) {
            try {
                reservationOld = changeReservationStatus(reservationOld, reservation);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot change reservation");
            }
        }
        return reservationOld;
    }

    @Transactional
    Reservation changeLane(Reservation reservationOld, Reservation reservationNew) {
        Parking parking = pr.findOneById(reservationNew.getParkingId());
        parking.freeSpot(reservationOld.getLaneName());
        parking.reserveSpot(reservationNew.getLaneName());
        pr.save(parking);
        return rp.save(reservationNew);
    }

    @Transactional
    Reservation changeParking(Reservation reservationOld, Reservation reservationNew) {
        Parking parkingNew = pr.findOneById(reservationNew.getParkingId());
        Parking parkingOld = pr.findOneById(reservationOld.getParkingId());
        boolean result1 = parkingNew.reserveSpot(reservationNew.getLaneName());
        boolean result2 = parkingOld.freeSpot(reservationOld.getLaneName());

        if (!result1 || !result2) {
            return null;
        }
        pr.save(parkingNew);
        pr.save(parkingOld);
        return rp.save(reservationNew);
    }

    private Reservation changeReservationStatus(Reservation reservationOld, Reservation reservationNew) {
        LocalDateTime date = LocalDateTime.now();
        if (reservationOld.getReservationStatus().equals(ReservationStatus.RESERVED) &&
                reservationNew.getReservationStatus().equals(ReservationStatus.OCCUPIED)) {
            reservationNew.setReservationEnd(date);
            reservationNew.setOccupationStart(date);
        } else if (reservationOld.getReservationStatus().equals(ReservationStatus.OCCUPIED) &&
        reservationNew.getReservationStatus().equals(ReservationStatus.ENDED)) {
            reservationNew.setOccupationEnd(date);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal state change");
        }
        return rp.save(reservationNew);
    }

    //todo validation
    public boolean extendReservation(String id, int minutes) {
        Reservation reservation = rp.findFirstById(id);
        if (reservation.getOccupationStart() == null) {
            reservation.setReservationEnd(reservation.getReservationEnd().plusMinutes(minutes));
            rp.save(reservation);
            return true;
        }
        return false;
    }




}
