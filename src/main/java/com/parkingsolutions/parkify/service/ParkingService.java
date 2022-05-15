package com.parkingsolutions.parkify.service;

import com.parkingsolutions.parkify.document.Parking;
import com.parkingsolutions.parkify.repository.ParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ParkingService {

    private final ParkingRepository pr;

    @Autowired
    public ParkingService(ParkingRepository pr) {
        this.pr = pr;
    }

    public List<Parking> getAll() {
        return pr.findAll();
    }

    public Parking add(Parking parking) {
        return pr.insert(parking);
    }

    public Parking getOneById(String id) {
        return pr.findFirstById(id);
    }

    public Parking getOneByCity(String city) {
        return pr.findAllByCity(city);
    }

    public List<Parking> getAllByOwnerId(String ownerId) {
        return pr.findAllByOwnerId(ownerId);
    }

}
