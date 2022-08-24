package com.parkingsolutions.parkify.repository;

import com.parkingsolutions.parkify.document.Parking;
import org.springframework.data.geo.Circle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring data Mongo Repository for Parking collection.
 */
@Repository
public interface ParkingRepository extends MongoRepository<Parking, String> {
    /**
     * Get all parkings
     * @return every parking in the database
     * @see Parking
     */
    List<Parking> findAll();

    /**
     * Find all parkings that belongs to given owner
     * @param ownerId Owner recognized using email
     * @return List of owners' parkings
     * @see Parking
     */
    List<Parking> findAllByOwnerId(String ownerId);

    /**
     * Find all parkings in given city
     * @param city
     * @return List of parkings in given city
     * @see Parking
     */
    List<Parking> findAllByCity(String city);

    /**
     * Find all parkings in given city where free spots number is greater than given number
     * @param city
     * @param number number of free spots at parking
     * @return List of parkings in given city
     * @see Parking
     */
    List<Parking> findAllByCityAndAvailableSpotsIsGreaterThan(String city, int number);

    /**
     * Find specific parking with given id
     * @param id
     * @return Parking if exists
     * @see Parking
     */
    Parking findOneById(String id);

    /**
     * Check if parking with given name exists. If not add it to db.
     * @param parking
     * @return Parking if insert completed successfully
     * @see Parking
     */
    Parking insert(Parking parking);

    /**
     * Add new Parking or update existing
     * @param parking
     * @return New or updated instance
     * @see Parking
     */
    Parking save(Parking parking);

    /**
     * Delete parking
     * @param id
     */
    void deleteById(String id);

    /**
     * Find all Parking with available spots that are in given area
     * @param location longitude, latitude in degrees and radius in kilometres
     * @param number number of required free spots
     * @return list of parking matching criteria
     * @see Parking
     */
    List<Parking> findAllByLocationIsWithinAndAvailableSpotsIsGreaterThan(Circle location, int number);
}
