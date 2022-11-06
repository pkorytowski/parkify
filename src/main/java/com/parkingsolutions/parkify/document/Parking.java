package com.parkingsolutions.parkify.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Parking document representation from the database
 */
@Document("parking")
@AllArgsConstructor
@NoArgsConstructor
@Data
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
}
