package com.parkingsolutions.parkify.scheduled;

import com.parkingsolutions.parkify.common.ReservationStatus;
import com.parkingsolutions.parkify.document.Parking;
import com.parkingsolutions.parkify.document.Reservation;
import com.parkingsolutions.parkify.repository.ParkingRepository;
import com.parkingsolutions.parkify.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@EnableScheduling
@EnableAsync
public class ReservationHandler {

    private final ReservationRepository rp;
    private final ParkingRepository pr;

    @Autowired
    public ReservationHandler(ReservationRepository rp, ParkingRepository pr) {
        this.rp = rp;
        this.pr = pr;
    }

    @Scheduled(fixedRate = 5000)
    @Async
    public void cancelReservations() {
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> reservations = rp.findAllByReservationStatusEqualsAndReservationEndIsBefore(
                ReservationStatus.RESERVED, now);

        for (Reservation reservation: reservations) {
            cancelSingleReservation(reservation);
        }
    }

    @Transactional
    public void cancelSingleReservation(Reservation reservation) {
        Parking parking = pr.findOneById(reservation.getParkingId());
        parking.freeSpot();
        reservation.setReservationStatus(ReservationStatus.CANCELED);
        reservation.setReservationEnd(LocalDateTime.now());
        pr.save(parking);
        rp.save(reservation);
    }

}
