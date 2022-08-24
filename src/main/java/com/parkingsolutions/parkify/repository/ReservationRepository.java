package com.parkingsolutions.parkify.repository;

import com.parkingsolutions.parkify.common.ReservationStatus;
import com.parkingsolutions.parkify.document.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring data Mongo Repository for Reservation collection
 */
@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {

    /**
     * Find all reservations. Not recommended to use.
     * @return all reservations in db
     * @see Reservation
     */
    List<Reservation> findAll();

    /**
     * Find all reservations for user
     * @param id user email
     * @return List of user's resrvations
     * @see Reservation
     */
    List<Reservation> findAllByUserId(String id);

    /**
     * Find all reservations for user and status
     * @param userId user email
     * @param reservationStatus status
     * @return List of reservations that matching criteria
     * @see ReservationStatus
     * @see Reservation
     */
    List<Reservation> findAllByUserIdAndReservationStatusEquals(String userId, ReservationStatus reservationStatus);

    /**
     * Find all reservations in parking
     * @param id parking Id
     * @return List reservations that matching criteria
     * @see Reservation
     */
    List<Reservation> findAllByParkingId(String id);

    /**
     * Find reservation with given Id
     * @param id of reservation
     * @return Reservation
     * @see Reservation
     */
    Reservation findFirstById(String id);

    /**
     * Add new reservation
     * @param reservation
     * @return saved instance
     * @see Reservation
     * @see ReservationRepository#save(Reservation)
     */
    Reservation insert(Reservation reservation);

    /**
     * Add new reservation or create existing
     * @param reservation
     * @return saved or updated instance
     * @see Reservation
     */
    Reservation save(Reservation reservation);

    /**
     * Return reservation that reservation end is before given date
     * @param status status
     * @param date target reservation end
     * @return
     * @see ReservationStatus
     * @see Reservation
     */
    List<Reservation> findAllByReservationStatusEqualsAndReservationEndIsBefore(ReservationStatus status,
                                                                               LocalDateTime date);

}
