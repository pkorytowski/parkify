package com.parkingsolutions.parkify.document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private int size;
    private int availableSpots;
    private List<Lane> lanes;

    public Parking() {}

    @JsonCreator
    public Parking(@JsonProperty("ownerid") String ownerId,
                   @JsonProperty("name") String name,
                   @JsonProperty("city") String city,
                   @JsonProperty("street") String street,
                   @JsonProperty("number") String number,
                   @JsonProperty("postalcode") String postalcode,
                   @JsonProperty("country") String country,
                   @JsonProperty("lanes") List<Lane> lanes){
        this.ownerId = ownerId;
        this.name = name;
        this.city = city;
        this.street = street;
        this.number = number;
        this.postalcode = postalcode;
        this.country = country;
        setLanes(lanes);
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

    public List<Lane> getLanes() {
        return lanes;
    }

    public void setLanes(List<Lane> lanes) {
        this.lanes = lanes;
        size = 0;
        availableSpots = 0;
        Set<String> laneNames = new HashSet<>();
        for (Lane lane: this.lanes) {
            size += lane.getSize();
            availableSpots += lane.getAvailableSpots();
            laneNames.add(lane.getName());
        }
        if (laneNames.size() != this.lanes.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lane name be unique within parking");
        }
    }

    public boolean reserveSpot(String laneName) {
        if (availableSpots <= 0) {
            return false;
        }
        for (Lane lane: lanes) {
            if (lane.getName().equals(laneName)) {
                if (lane.getAvailableSpots() <= 0) {
                    return false;
                } else {
                    lane.setAvailableSpots(lane.getAvailableSpots()-1);
                    availableSpots--;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean freeSpot(String laneName) {
        for (Lane lane: lanes) {
            if (lane.getName().equals(laneName)) {
                availableSpots++;
                lane.setAvailableSpots(lane.getAvailableSpots()+1);
                return true;
            }
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
