package com.parkingsolutions.parkify.document;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.parkingsolutions.parkify.common.ParkingSpotStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParkingSpot {
    @Id
    private String id;

    private int number;
    private ParkingSpotStatus status;
    private String occupiedUserId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime occupationEnd;
}
