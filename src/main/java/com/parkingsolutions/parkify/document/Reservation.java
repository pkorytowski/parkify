package com.parkingsolutions.parkify.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.parkingsolutions.parkify.common.ReservationStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Document("reservation")
public class Reservation implements Serializable {
    @Id
    private String id;
    private String userId;
    private String parkingId;
    private String laneName;

    private ReservationStatus reservationStatus;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reservationStart;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reservationEnd;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime occupationStart;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime occupationEnd;

    public Reservation() {}
    public Reservation(String userId,
                       String parkingId,
                       String laneName,
                       ReservationStatus reservationStatus,
                       LocalDateTime reservationStart,
                       LocalDateTime reservationEnd,
                       LocalDateTime occupationStart,
                       LocalDateTime occupationEnd) {
        this.userId = userId;
        this.parkingId = parkingId;
        this.laneName = laneName;
        this.reservationStatus = reservationStatus;
        this.reservationStart = reservationStart;
        this.reservationEnd = reservationEnd;
        this.occupationStart = occupationStart;
        this.occupationEnd = occupationEnd;
    }

    public Reservation(Reservation reservation) {
        this.id = reservation.id;
        this.userId = reservation.userId;
        this.parkingId = reservation.parkingId;
        this.laneName = reservation.laneName;
        this.reservationStatus = reservation.reservationStatus;
        this.reservationStart = reservation.reservationStart;
        this.reservationEnd = reservation.reservationEnd;
        this.occupationStart = reservation.occupationStart;
        this.occupationEnd = reservation.occupationEnd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public String getLaneName() {
        return laneName;
    }

    public void setLaneName(String laneName) {
        this.laneName = laneName;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public LocalDateTime getReservationStart() {
        return reservationStart;
    }

    public void setReservationStart(LocalDateTime reservationStart) {
        this.reservationStart = reservationStart;
    }

    public LocalDateTime getReservationEnd() {
        return reservationEnd;
    }

    public void setReservationEnd(LocalDateTime reservationEnd) {
        this.reservationEnd = reservationEnd;
    }

    public LocalDateTime getOccupationStart() {
        return occupationStart;
    }

    public void setOccupationStart(LocalDateTime occupationStart) {
        this.occupationStart = occupationStart;
    }

    public LocalDateTime getOccupationEnd() {
        return occupationEnd;
    }

    public void setOccupationEnd(LocalDateTime occupationEnd) {
        this.occupationEnd = occupationEnd;
    }

}
