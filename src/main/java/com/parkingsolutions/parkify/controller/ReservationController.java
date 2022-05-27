package com.parkingsolutions.parkify.controller;

import com.parkingsolutions.parkify.document.Reservation;
import com.parkingsolutions.parkify.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("reservation")
public class ReservationController {
    private final ReservationService rs;

    @Autowired
    public ReservationController(ReservationService rs) {
        this.rs = rs;
    }

    @GetMapping("/all")
    public List<Reservation> getAll(@RequestParam String userId,
                                    @RequestParam String parkingId) {
        if (userId != null) {
            return rs.getAllByUserId(userId);
        } else if (parkingId != null) {
            return rs.getAllByParkingId(parkingId);
        }
        return rs.getAll();
    }

    @PostMapping
    public Reservation add(@RequestBody Reservation reservation) {
        return rs.save(reservation);
    }

    @PutMapping
    public Reservation update(@RequestBody Reservation reservation) {
        return rs.updateReservation(reservation);
    }



    /*
    @GetMapping("/all/{userId}")
    public List<Reservation> getAllByUserId(@PathVariable String userId) {
        return rs.getAllByUserId(userId);
    }

    @GetMapping("/all")
    */

}
