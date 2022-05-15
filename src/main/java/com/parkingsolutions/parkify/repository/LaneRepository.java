package com.parkingsolutions.parkify.repository;

import com.parkingsolutions.parkify.document.Lane;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaneRepository extends MongoRepository<Lane, String> {
    List<Lane> findAllByParkingId(String parkingId);

}
