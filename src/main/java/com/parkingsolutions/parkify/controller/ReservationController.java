package com.parkingsolutions.parkify.controller;

import com.parkingsolutions.parkify.bean.ReservationFull;
import com.parkingsolutions.parkify.common.ReservationStatus;
import com.parkingsolutions.parkify.document.Reservation;
import com.parkingsolutions.parkify.service.ReservationService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("reservation")
public class ReservationController {
    private final ReservationService rs;

    private String SECRET = "mySecretKey";
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
        return rs.getActiveFullReservationsByUserId(user);
    }

    @PutMapping("extend")
    public boolean extendReservation(@RequestBody Map<String, String> request) {
        String id = request.get("id");
        int time = Integer.parseInt(request.get("time"));

        boolean result = rs.extendReservation(id, time);
        if (result) {
            throw new ResponseStatusException(HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("status")
    public boolean changeStatus(@RequestBody Map<String, String> request) {
        String id = request.get("id");
        String statusStr = request.get("status");
        ReservationStatus status;
        switch (statusStr) {
            case "RESERVED":
                status = ReservationStatus.RESERVED;
                break;
            case "OCCUPIED":
                status = ReservationStatus.OCCUPIED;
                break;
            case "ENDED":
                status = ReservationStatus.ENDED;
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return rs.changeReservationStatus(id, status);
    }
    /*
    @GetMapping("/all/{userId}")
    public List<Reservation> getAllByUserId(@PathVariable String userId) {
        return rs.getAllByUserId(userId);
    }

    @GetMapping("/all")
    */

}
