package com.parkingsolutions.parkify.repository;

import com.parkingsolutions.parkify.document.Parking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingRepository extends MongoRepository<Parking, String> {
    List<Parking> findAll();
    List<Parking> findAllByOwnerId(String ownerId);
    List<Parking> findAllByCity(String city);
    List<Parking> findAllByCityAndAvailableSpotsIsGreaterThan(String city, int number);
    Parking findFirstById(String id);
    Parking insert(Parking parking);
    Parking save(Parking parking);
    void deleteById(String id);
}
