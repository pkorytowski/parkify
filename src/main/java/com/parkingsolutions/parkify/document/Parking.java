package com.parkingsolutions.parkify.document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Parking document representation from the database
 */
@Document("parking")
public class Parking {
    @Id
    private String id;

    private String ownerId;
    private String name;
    private String city;
    private String street;
    private String number;
    private String postalcode;
    private String country;
    private Point location;
    private int size;
    private int availableSpots;

    public Parking() {}

    @JsonCreator
    public Parking(@JsonProperty("ownerid") String ownerId,
                   @JsonProperty("name") String name,
                   @JsonProperty("city") String city,
                   @JsonProperty("street") String street,
                   @JsonProperty("number") String number,
                   @JsonProperty("postalcode") String postalcode,
                   @JsonProperty("country") String country,
                   @JsonProperty("location") Point location,
                   @JsonProperty("size") int size) {
        this.ownerId = ownerId;
        this.name = name;
        this.city = city;
        this.street = street;
        this.number = number;
        this.postalcode = postalcode;
        this.country = country;
        this.location = location;
        this.size = size;
        this.availableSpots = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public boolean reserveSpot() {
        if (availableSpots <= 0) {
            return false;
        }
        availableSpots--;
        return true;
    }

    public boolean freeSpot() {
        if (availableSpots < size) {
            availableSpots++;
            return true;
        }
        return false;
    }

    public int getAvailableSpots() {
        return availableSpots;
    }

    public void setAvailableSpots(int availableSpots) {
        this.availableSpots = availableSpots;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
