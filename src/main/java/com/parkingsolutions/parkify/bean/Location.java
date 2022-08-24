package com.parkingsolutions.parkify.bean;


import java.io.Serializable;

/**
 * Class for storing geo coordinated of parking
 */
public class Location implements Serializable {
    /**
     * Point is the type of location required by MongoDB
     */
    private final String type = "Point";
    /**
     * Geo coordinates with specific order [longitude, latitute]
     */
    private double[] coordinates = new double[2];

    /**
     * Get coordinates of parking address
     * @return coordinates in order [longitude, latitude]
     */
    public double[] getCoordinates() {
        return coordinates;
    }

    /**
     *
     * @param coordinates in order [longitude, latitude]
     */
    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }
}
