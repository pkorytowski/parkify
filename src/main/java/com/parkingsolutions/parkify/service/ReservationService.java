package com.parkingsolutions.parkify.service;

import com.parkingsolutions.parkify.bean.ReservationFull;
import com.parkingsolutions.parkify.common.ReservationStatus;
import com.parkingsolutions.parkify.document.Parking;
import com.parkingsolutions.parkify.document.Reservation;
import com.parkingsolutions.parkify.document.User;
import com.parkingsolutions.parkify.repository.ParkingRepository;
import com.parkingsolutions.parkify.repository.ReservationRepository;
import com.parkingsolutions.parkify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ReservationService {
    private final ReservationRepository rp;
    private final ParkingRepository pr;
    private final UserRepository ur;

    private final int DEFAULT_RESERVATION_EXTEND_TIME = 15;

    @Autowired
    public ReservationService(ReservationRepository rp, ParkingRepository pr, UserRepository ur) {
        this.rp = rp;
        this.pr = pr;
        this.ur  = ur;
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

    public List<ReservationFull> getOneActiveFullReservationByUserId(String id) {
        List<ReservationFull> activeReservations = getActiveFullReservationsByUserId(id);
        System.out.println(activeReservations);
        if (activeReservations.isEmpty() || activeReservations.size() == 1) {
            return activeReservations;
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Incorrect number of active reservations");
        }
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
        System.out.println(reservedReservations);
        return Stream.of(reservedReservations, occupiedReservations)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public Reservation getOneById(String id) {
        return rp.findFirstById(id);
    }

    @Transactional
    public Reservation save(Reservation reservation, String id) {
        List<Reservation> alreadyReserved = rp.findAllByUserIdAndReservationStatusEquals(id, ReservationStatus.RESERVED);
        if (alreadyReserved.size() > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Another spot already reserved");
        }
        List<Reservation> alreadyOccupied = rp.findAllByUserIdAndReservationStatusEquals(id, ReservationStatus.OCCUPIED);
        if (alreadyOccupied.size() > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Another spot already occupied");
        }
        Parking parking = pr.findOneById(reservation.getParkingId());
        if (reservation.getReservationStatus() == null) {
            reservation.setUserId(id);
            reservation.setReservationStatus(ReservationStatus.RESERVED);
            reservation.setReservationStart(LocalDateTime.now());
            reservation.setReservationEnd(LocalDateTime.now().plusMinutes(DEFAULT_RESERVATION_EXTEND_TIME));
        }
        boolean result = parking.reserveSpot();
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
    Reservation changeParking(Reservation reservationOld, Reservation reservationNew) {
        Parking parkingNew = pr.findOneById(reservationNew.getParkingId());
        Parking parkingOld = pr.findOneById(reservationOld.getParkingId());
        boolean result1 = parkingNew.reserveSpot();
        boolean result2 = parkingOld.freeSpot();

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
        } else if (reservationOld.getReservationStatus().equals(ReservationStatus.RESERVED) &&
                reservationNew.getReservationStatus().equals(ReservationStatus.CANCELED)) {
            reservationNew.setReservationEnd(date);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal status change");
        }
        return rp.save(reservationNew);
    }

    public void occupySpot(String id, String dateStr) {
        Reservation reservation = rp.findFirstById(id);
        if (reservation.getReservationStatus() == ReservationStatus.RESERVED) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dt = LocalDateTime.parse(dateStr, formatter);
            if (dt.isAfter(now)) {
                reservation.setReservationStatus(ReservationStatus.OCCUPIED);
                reservation.setReservationEnd(now);
                reservation.setOccupationStart(now);
                reservation.setOccupationEnd(dt);
                rp.save(reservation);
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);    }

    public boolean changeReservationStatus(String id, ReservationStatus reservationStatus) {
        Reservation reservationOld = rp.findFirstById(id);
        Reservation reservationNew = new Reservation(reservationOld);
        reservationNew.setReservationStatus(reservationStatus);
        Reservation result = changeReservationStatus(reservationOld, reservationNew);
        return result.getReservationStatus().equals(reservationNew.getReservationStatus());
    }

    //todo validation
    public void extendReservation(String id) {
        Reservation reservation = rp.findFirstById(id);
        if (reservation.getReservationStatus() == ReservationStatus.RESERVED) {
            reservation.setReservationEnd(reservation.getReservationEnd().plusMinutes(DEFAULT_RESERVATION_EXTEND_TIME));
            rp.save(reservation);
        } else if (reservation.getReservationStatus() == ReservationStatus.OCCUPIED) {
            reservation.setOccupationEnd(reservation.getOccupationEnd().plusMinutes(DEFAULT_RESERVATION_EXTEND_TIME));
            rp.save(reservation);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public void endReservation(String id) {
        Reservation reservation =  rp.findFirstById(id);
        Parking parking = pr.findOneById(reservation.getParkingId());
        User user = ur.findOneByEmail(reservation.getUserId());
        if (reservation.getReservationStatus() == ReservationStatus.OCCUPIED) {
            LocalDateTime datetime = LocalDateTime.now();
            if (datetime.isAfter(reservation.getOccupationEnd())) {
                user.decreaseRank();
            } else {
                user.increaseRank();
            }
            reservation.setOccupationEnd(LocalDateTime.now());
            reservation.setReservationStatus(ReservationStatus.ENDED);
            parking.freeSpot();
            ur.save(user);
            pr.save(parking);
            rp.save(reservation);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public void cancelReservation(String id) {
        Reservation reservation =  rp.findFirstById(id);
        Parking parking = pr.findOneById(reservation.getParkingId());
        if (reservation.getReservationStatus() == ReservationStatus.RESERVED) {
            reservation.setReservationStatus(ReservationStatus.CANCELED);
            reservation.setReservationEnd(LocalDateTime.now());
            parking.freeSpot();
            pr.save(parking);
            rp.save(reservation);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

}
