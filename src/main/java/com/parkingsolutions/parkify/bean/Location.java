package com.parkingsolutions.parkify.bean;


import java.io.Serializable;

public class Location implements Serializable {
    private final String type = "Point";
    private double[] coordinates = new double[2];

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }
}
