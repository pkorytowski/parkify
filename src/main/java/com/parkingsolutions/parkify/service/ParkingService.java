package com.parkingsolutions.parkify.service;

import com.parkingsolutions.parkify.bean.AvailableSpot;
import com.parkingsolutions.parkify.document.Lane;
import com.parkingsolutions.parkify.document.Parking;
import com.parkingsolutions.parkify.repository.ParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        Set<String> laneNames = new HashSet<>();
        for (Lane lane: parking.getLanes()) {
            laneNames.add(lane.getName());
        }
        if (laneNames.size() != parking.getLanes().size()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Name of lanes must by unique within parking");
        }
        return pr.insert(parking);
    }

    public Parking getOneById(String id) {
        return pr.findFirstById(id);
    }

    public List<Parking> getAllByCity(String city) {
        return pr.findAllByCity(city);
    }

    public List<Parking> getAllByOwnerId(String ownerId) {
        return pr.findAllByOwnerId(ownerId);
    }

    public void deleteOneById(String id) {
        pr.deleteById(id);
    }

    public List<AvailableSpot> getFreeByCity(String city) {
        List<Parking> parkings = pr.findAllByCity(city);

        return null;
    }

}
