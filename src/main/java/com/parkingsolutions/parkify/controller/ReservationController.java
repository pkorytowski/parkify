package com.parkingsolutions.parkify.controller;

import com.parkingsolutions.parkify.bean.ReservationFull;
import com.parkingsolutions.parkify.document.Reservation;
import com.parkingsolutions.parkify.service.ReservationService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("reservation")
public class ReservationController {
    private final ReservationService rs;

    @Value("${secret}")
    private String SECRET = "mySecretKey";

    @Value("${prefix}")
    private String PREFIX = "Parkify ";

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

    @GetMapping("full")
    public List<ReservationFull> getFullReservationsByUserId(@RequestHeader (name = "Authorization") String token) {
        String user = Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token.replace(PREFIX, ""))
                .getBody()
                .getSubject();

        return rs.getFullReservationsByUserId(user);
    }

    @GetMapping("active")
    public List<ReservationFull> getActiveFullReservationByUserId(@RequestHeader (name = "Authorization") String token) {
        String user = Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token.replace(PREFIX, ""))
                .getBody()
                .getSubject();
        return rs.getFullReservationsByUserIdAndReservationStatusEqualsActive(user);
    }

    /*
    @GetMapping("/all/{userId}")
    public List<Reservation> getAllByUserId(@PathVariable String userId) {
        return rs.getAllByUserId(userId);
    }

    @GetMapping("/all")
    */

}
