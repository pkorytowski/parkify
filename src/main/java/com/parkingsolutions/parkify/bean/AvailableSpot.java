package com.parkingsolutions.parkify.bean;

public class AvailableSpot {
    private String parkingId;
    private String lane;
    private int availableSpots;

    public AvailableSpot(String parkingId, String lane, int availableSpots) {
        this.parkingId = parkingId;
        this.lane = lane;
        this.availableSpots = availableSpots;
    }

    public int getAvailableSpots() {
        return availableSpots;
    }

    public void setAvailableSpots(int availableSpots) {
        this.availableSpots = availableSpots;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }
}
