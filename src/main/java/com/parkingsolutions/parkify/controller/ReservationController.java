package com.parkingsolutions.parkify.controller;

import com.parkingsolutions.parkify.common.ReservationStatus;
import com.parkingsolutions.parkify.document.Reservation;
import com.parkingsolutions.parkify.service.ReservationService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

/**
 * Rest controller for CRUD operations on Reservation instances
 */
@RestController
@RequestMapping("reservation")
public class ReservationController {
    private final ReservationService rs;

    private final String SECRET = "mySecretKey";
    private final String PREFIX = "Parkify ";

    @Autowired
    public ReservationController(ReservationService rs) {
        this.rs = rs;
    }
/*
    @GetMapping("/all")
    public List<Reservation> getAll(@RequestHeader (name = "Authorization") String token) {
        String user = Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token.replace(PREFIX, ""))
                .getBody()
                .getSubject();
        return rs.getAllByUserId(user);
    }
*/

    /**
     * Create new reservation
     * @param reservation with details parkingId, userId, reservationDate
     * @param token included in Authorization header
     * @return Saved reservation instance
     */
    @PostMapping
    public Reservation add(@RequestBody Reservation reservation, @RequestHeader (name = "Authorization") String token) {
        String user = Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token.replace(PREFIX, ""))
                .getBody()
                .getSubject();
        //return rs.save(reservation, user);
        return null;
    }

    /*
    @PutMapping
    public Reservation update(@RequestBody Reservation reservation) {
        return rs.updateReservation(reservation);
    }

     */

    /**
     * Get active reservation for user. It is possible to have only one active reservation at the time. It is a workaround for client app
     * @param token authorization token
     * @return Empty list when there is no active reservations. List with length otherwise
     */


    @GetMapping
    public List<Reservation> getReservation(@RequestHeader (name = "Authorization") String token) {
        String user = Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token.replace(PREFIX, ""))
                .getBody()
                .getSubject();
        //return rs.getOneActiveFullReservationByUserId(user);
        return null;
    }



    /**
     * Extend reservation
     * @param request
     */
    @PutMapping("extend")
    public void extendReservation(@RequestBody Map<String, String> request) {
        String id = request.get("id");
        if (id != null) {
            //rs.extendReservation(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("occupy")
    public void occupySpot(@RequestBody Map<String, String> request) {
        String id = request.get("id");
        String dateStr = request.get("predictedReservationEnd");
        if (id != null) {
           //rs.occupySpot(id, dateStr);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("end")
    public void endReservation(@RequestBody Map<String, String> request) {
        String id = request.get("id");
        if (id != null) {
            //rs.endReservation(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("cancel")
    public void cancelReservation(@RequestBody Map<String, String> request) {
        String id = request.get("id");
        if (id != null) {
            //rs.cancelReservation(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
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
            case "CANCELED":
                status = ReservationStatus.CANCELED;
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        //return rs.changeReservationStatus(id, status);
        return true;
    }

}
