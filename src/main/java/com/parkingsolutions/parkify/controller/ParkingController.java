package com.parkingsolutions.parkify.controller;

import com.parkingsolutions.parkify.bean.AvailableSpot;
import com.parkingsolutions.parkify.document.Parking;
import com.parkingsolutions.parkify.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("parking")
public class ParkingController {
    private final ParkingService ps;

    @Autowired
    public ParkingController(ParkingService ps) {
        this.ps = ps;
    }

    @GetMapping("/all")
    public List<Parking> getAll() {
        return ps.getAll();
    }

    @GetMapping("/all/{ownerId}")
    public List<Parking> getAllByOwnerId(@PathVariable String ownerId) {
        return ps.getAllByOwnerId(ownerId);
    }

    @PostMapping
    public Parking add(@RequestBody Parking parking) {
        return ps.add(parking);
    }

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
    public List<AvailableSpot> getFreeByCity(@PathVariable String city) {
        return ps.getFreeByCity(city);
    }
}
