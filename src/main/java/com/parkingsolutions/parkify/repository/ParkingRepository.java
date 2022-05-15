package com.parkingsolutions.parkify.repository;

import com.parkingsolutions.parkify.document.Parking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingRepository extends MongoRepository<Parking, String> {
    List<Parking> findAll();
    List<Parking> findAllByOwnerId(String ownerId);
    Parking findFirstById(String id);
    List<Parking> findAllByCity(String city);
    Parking insert(Parking parking);
    Parking save(Parking parking);
    void deleteById(String id);
}
