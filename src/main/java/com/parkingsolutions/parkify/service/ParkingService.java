package com.parkingsolutions.parkify.service;

import com.parkingsolutions.parkify.document.Parking;
import com.parkingsolutions.parkify.repository.ParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Point;
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

    public Parking addSpecific() {
        Parking parking = new Parking(
                "6280d84995e1e9d7f1350141",
                "Parking nr 2",
                "Krakow",
                "Al. Mickiewicza",
                "22",
                "22-222",
                "Poland",
                new Point(19.914240, 50.066330),
                10
        );

        return add(parking);
    }

    public Parking add(Parking parking) {
        return pr.insert(parking);
    }
/*
    public Parking getOneById(String id) {
        return pr.findOneById(id);
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

    public List<Parking> getFreeByCity(String city) {

        return pr.findAllByCityAndAvailableSpotsIsGreaterThan(city,0);

    }
*/
    public List<Parking> getFreeWithinLocation(double longitude, double latitude, double distance) {
        //double earthDist = distance/6371;
        Circle circle = new Circle(longitude, latitude, 5);
        return pr.findAllByLocationIsWithinAndAvailableSpotsIsGreaterThan(circle, 0);
    }

}
