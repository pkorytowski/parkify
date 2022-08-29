package com.parkingsolutions.parkify.service;

import com.parkingsolutions.parkify.document.Parking;
import com.parkingsolutions.parkify.document.Point;
import com.parkingsolutions.parkify.repository.ParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.mongodb.core.geo.Sphere;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Class that implements logic for Parking operations
 */
@Component
public class ParkingService {

    private final ParkingRepository pr;

    @Autowired
    public ParkingService(ParkingRepository pr) {
        this.pr = pr;
    }

    /**
     * Get all parkings in db
     * @return List of parkings
     * @see Parking
     */
    public List<Parking> getAll() {
        return pr.findAll();
    }

    public Parking addSpecific() {
        Parking parking = new Parking(
                "6280d84995e1e9d7f1350141",
                "Test Parking",
                "Krakow",
                "Al. Mickiewicza",
                "22",
                "22-222",
                "Poland",
                new Point(19.914240, 50.066330),
                0
        );

        return add(parking);
    }

    /**
     * Add new parking if similar does not exist
     * @param parking
     * @return Created parking instance if created
     * @see Parking
     */
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
*/
    public void deleteOneById(String id) {
        pr.deleteById(id);
    }
/*
    public List<Parking> getFreeByCity(String city) {

        return pr.findAllByCityAndAvailableSpotsIsGreaterThan(city,0);

    }
*/

    /**
     * Get all parkings from given area
     * @param longitude of center of the area
     * @param latitude of center of the area
     * @param distance from center
     * @return list of parkings
     * @see Parking
     * @see Circle
     */
    public List<Parking> getFreeWithinLocation(double longitude, double latitude, double distance) {
        double earthDist = distance/6371.0;
        Sphere sphere = new Sphere(new Circle(longitude, latitude, earthDist));
        return pr.findAllByLocationIsWithinAndAvailableSpotsIsGreaterThanOrderByLocationAsc(sphere, 0);
    }

}
