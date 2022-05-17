package com.parkingsolutions.parkify.bean;

import java.util.ArrayList;
import java.util.List;

public class AvailableParking {
    private String parkingId;

    private List<AvailableSpot> availableSpots = new ArrayList<>();

    public AvailableParking(String parkingId) {
        this.parkingId = parkingId;
    }

    public void add(AvailableSpot availableSpot) {
        this.availableSpots.add(availableSpot);
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public List<AvailableSpot> getAvailableSpots() {
        return availableSpots;
    }

    public void setAvailableSpots(List<AvailableSpot> availableSpots) {
        this.availableSpots = availableSpots;
    }

}
