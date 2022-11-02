package com.parkingsolutions.parkify.controller;

import com.parkingsolutions.parkify.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller for CRUD on Parking instances
 */
@RestController
@RequestMapping("parking")
public class ParkingController {
    private final ParkingService ps;

    @Autowired
    public ParkingController(ParkingService ps) {
        this.ps = ps;
    }

    /*@GetMapping("/all")
    public List<Parking> getAll() {
        return ps.getAll();
    }*/

    /*
    @GetMapping("/all/{ownerId}")
    public List<Parking> getAllByOwnerId(@PathVariable String ownerId) {
        return ps.getAllByOwnerId(ownerId);
    }

     */

    /**
     * Create new parking
     * @param parking
     * @return Saved instance
     */
    /*@PostMapping
    public Parking add(@RequestBody Parking parking) {
        return ps.add(parking);
    }*/

    /*
    @GetMapping("/{id}")
    public Parking getOneById(@PathVariable String id) {
        if (id != null) {
            return ps.getOneById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/{id}")
    public void deleteOneById(@PathVariable String id) {
        ps.deleteOneById(id);
    }

    @GetMapping("/city/{city}")
    public List<Parking> getAllByCity(@PathVariable String city) {
        return ps.getAllByCity(city);
    }

    @GetMapping("/city/{city}/free")
    public List<Parking> getFreeByCity(@PathVariable String city) {
        return ps.getFreeByCity(city);
    }

     */

    /**
     * Get all Parkings with at least one free spot in given area.
     * @param longitude geo coordinate
     * @param latitude geo coordinates
     * @param distance in kilometres
     * @return List of parking in given area.
     */
    /*@GetMapping("find")
    public List<Parking> findAllFreeInDistance(@RequestParam("lon") double longitude,
                                               @RequestParam("lat") double latitude,
                                               @RequestParam("distance") double distance) {



        return ps.getFreeWithinLocation(longitude, latitude, distance);
    }*/

   /* @PostMapping("specific")
    public Parking addSpecific(){
        return ps.addSpecific();

    }*/
}
