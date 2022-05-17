package com.parkingsolutions.parkify.bean;

public class AvailableSpot {
    private String laneId;
    private int availableSpots;

    public AvailableSpot(String laneId, int availableSpots) {
        this.laneId = laneId;
        this.availableSpots = availableSpots;
    }

    public int getAvailableSpots() {
        return availableSpots;
    }

    public void setAvailableSpots(int availableSpots) {
        this.availableSpots = availableSpots;
    }

    public String getLaneId() {
        return laneId;
    }

    public void setLaneId(String laneId) {
        this.laneId = laneId;
    }

}
