package com.parkingsolutions.parkify.repository;

import com.parkingsolutions.parkify.document.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {
    List<Reservation> findAll();
    List<Reservation> findAllByUserId(String id);
    List<Reservation> findAllByParkingId(String id);
    Reservation findFirstById(String id);
    Reservation insert(Reservation reservation);
    Reservation save(Reservation reservation);

}
