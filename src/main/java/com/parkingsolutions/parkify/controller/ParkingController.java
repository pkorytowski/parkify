package com.parkingsolutions.parkify.controller;

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

    @GetMapping
    public Parking getOne(@RequestParam(name = "id", required = false) String id,
                          @RequestParam(name = "city", required = false) String city) {
        if (id != null) {
            return ps.getOneById(id);
        } else if (city != null) {
            return ps.getOneByCity(city);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
