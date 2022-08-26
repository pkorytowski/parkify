package com.parkingsolutions.parkify.service;

import com.parkingsolutions.parkify.bean.ReservationFull;
import com.parkingsolutions.parkify.common.ReservationStatus;
import com.parkingsolutions.parkify.document.Parking;
import com.parkingsolutions.parkify.document.Reservation;
import com.parkingsolutions.parkify.document.User;
import com.parkingsolutions.parkify.repository.ParkingRepository;
import com.parkingsolutions.parkify.repository.ReservationRepository;
import com.parkingsolutions.parkify.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class that implements logic for Reservation operations
 */
@Component
public class ReservationService {

    private String SECRET = "mySecretKey";
    private String PREFIX = "Parkify ";
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

    /**
     * Get all reservations from db
     * @return List of reservations
     * @see Reservation
     */
    public List<Reservation> getAll() {
        return rp.findAll();
    }

    /**
     * Get all reservations belonging to user
     * @param id
     * @return List of reservations
     * @see Reservation
     */
    public List<Reservation> getAllByUserId(String id) {
        return rp.findAllByUserId(id);
    }

    /**
     * Get all reservations with given status belonging to user
     * @param id
     * @param reservationStatus
     * @return List of reservations
     * @see Reservation
     * @see ReservationStatus
     */
    public List<Reservation> getAllByUserIdAndReservationStatusEquals(String id, ReservationStatus reservationStatus) {
        return rp.findAllByUserIdAndReservationStatusEquals(id, reservationStatus);
    }

    /*
    public List<Reservation> getAllByParkingId(String id) {
        return rp.findAllByParkingId(id);
    }
*/

    /**
     * Get full reservations belonging to user
     * @param id
     * @return List of reservations with full information
     * @see ReservationFull
     */
    public List<ReservationFull> getFullReservationsByUserId(String id) {
        List<Reservation> reservations = getAllByUserId(id);
        List<ReservationFull> reservationFullList = new ArrayList<>();
        for (Reservation reservation: reservations) {
            Parking tmpParking = pr.findOneById(reservation.getParkingId());
            reservationFullList.add(new ReservationFull(reservation, tmpParking));
        }
        return reservationFullList;
    }

    /**
     * Get full reservations with status "RESERVED" belonging to user
     * @param id
     * @return List of reservations with full information
     * @see ReservationFull
     * @see ReservationStatus
     */
    private List<ReservationFull> getFullReservationsByUserIdAndReservationStatusEqualsReserved(String id) {
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


    /**
     * Get full reservations with status "OCCUPIED" belonging to user
     * @param id
     * @return List of reservations with full information
     * @see ReservationFull
     * @see ReservationStatus
     */
    private List<ReservationFull> getFullReservationsByUserIdAndReservationStatusEqualsOccupied(String id) {
        List<Reservation> reservations = getAllByUserIdAndReservationStatusEquals(id, ReservationStatus.OCCUPIED);
        List<ReservationFull> reservationFullList = new ArrayList<>();
        for (Reservation reservation: reservations) {
            Parking tmpParking = pr.findOneById(reservation.getParkingId());
            reservationFullList.add(new ReservationFull(reservation, tmpParking));
        }
        return reservationFullList;
    }

    /**
     * Get all active full reservations. Active means that they have status RESERVED or OCCUPIED
     * @param id of user
     * @return List of reservations with full information
     *      * @see ReservationFull
     *      * @see ReservationStatus
     */
    public List<ReservationFull> getActiveFullReservationsByUserId(String id) {
        List<ReservationFull> reservedReservations = getFullReservationsByUserIdAndReservationStatusEqualsReserved(id);
        List<ReservationFull> occupiedReservations = getFullReservationsByUserIdAndReservationStatusEqualsOccupied(id);
        System.out.println(reservedReservations);
        return Stream.of(reservedReservations, occupiedReservations)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
/*
    public Reservation getOneById(String id) {
        return rp.findFirstById(id);
    }
*/

    /**
     * Method that creates new reservation
     * @param reservation
     * @param id
     * @return Reservation instance if created
     * @see Reservation
     */
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
/*
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

 */

    /**
     * Change status of the reservation
     * @param reservationOld
     * @param reservationNew
     * @return
     */
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

    /**
     * Change reservation status from RESERVED to OCCUPIED
     * @param id
     * @param dateStr Expected occupation end
     * @see ReservationStatus
     */
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

    /**
     * Change reservation status
     * @param id
     * @param reservationStatus
     * @return
     */
    public boolean changeReservationStatus(String id, ReservationStatus reservationStatus) {
        Reservation reservationOld = rp.findFirstById(id);
        Reservation reservationNew = new Reservation(reservationOld);
        reservationNew.setReservationStatus(reservationStatus);
        Reservation result = changeReservationStatus(reservationOld, reservationNew);
        return result.getReservationStatus().equals(reservationNew.getReservationStatus());
    }

    //todo validation

    /**
     * Extend time of reservation
     * @param id
     * @see Reservation
     * @see ReservationService#DEFAULT_RESERVATION_EXTEND_TIME
     */
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

    /**
     * End reservation
     * @param id
     * @see Reservation
     * @see ReservationStatus
     */
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

    /**
     * Cancel reservation
     * @param id
     * @see Reservation
     * @see ReservationStatus
     */
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

    public void deleteAllReservations(@RequestHeader(name = "Authorization") String token) {
        String user = Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token.replace(PREFIX, ""))
                .getBody()
                .getSubject();



        rp.deleteAllByUserId(user);
    }

}
