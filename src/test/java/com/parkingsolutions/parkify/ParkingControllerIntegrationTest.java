package com.parkingsolutions.parkify;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkingsolutions.parkify.bean.AuthorizedUser;
import com.parkingsolutions.parkify.document.Parking;
import com.parkingsolutions.parkify.document.Point;
import com.parkingsolutions.parkify.service.ParkingService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@ActiveProfiles(value = "test")
@SpringBootTest(classes = {ParkifyApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ParkingControllerIntegrationTest {
    @LocalServerPort
    private int port;

    HttpHeaders headers = new HttpHeaders();

    TestRestTemplate restTemplate = new TestRestTemplate();

    private final List<String> parkingIds = new ArrayList<>();

    private final double[] myLocation = {-80.84392092202201, 35.22136711069161};

    private final ParkingService ps;

    @Autowired
    public ParkingControllerIntegrationTest(ParkingService ps) {
        this.ps = ps;
    }

    @BeforeAll
    public void performLogin() throws JsonProcessingException {
        Map<String, String> data = new HashMap<>();
        data.put("email", "email@email.pl");
        data.put("password", "password");
        ObjectMapper objectMapper = new ObjectMapper();
        String dataStr = null;
        try {
            dataStr = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        headers.add("Content-Type", "application/JSON");

        HttpEntity<String> entity = new HttpEntity<>(dataStr, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/login/user"),
                HttpMethod.POST, entity, String.class);

        AuthorizedUser user;

        user = objectMapper.readValue(response.getBody(), AuthorizedUser.class);
        headers.add("Authorization", user.getToken());
        setUpParkingsInDb();
    }

    @Test
    public void findParkingInRadius3km() throws JsonProcessingException {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/parking/find?lon=" + myLocation[0] + "&lat=" + myLocation[1] + "&distance=3"),
                HttpMethod.GET, entity, String.class);


        ObjectMapper objectMapper = new ObjectMapper();
        List<Parking> parkings = objectMapper.readValue(response.getBody(), new TypeReference<List<Parking>>(){});

        assertTrue(parkings.isEmpty());
    }

    @Test
    public void findParkingInRadius6point5km() throws JsonProcessingException {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/parking/find?lon=" + myLocation[0] + "&lat=" + myLocation[1] + "&distance=6.5"),
                HttpMethod.GET, entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        List<Parking> parkings = objectMapper.readValue(response.getBody(), new TypeReference<List<Parking>>(){});

        assertEquals(1, parkings.size());
    }
//6.58
    //3.10
    @AfterAll
    public void deleteAllParkings() {
        for(String id: parkingIds) {
            ps.deleteOneById(id);
        }
    }
    private void setUpParkingsInDb() {
        List<Parking> parkings = new ArrayList<>();
        parkings.add(new Parking(
                "6280d84995e1e9d7f1350141",
                "Walmart",
                "Charlotte",
                "Independence Blvd.",
                "2850 E",
                "NC 26205",
                "USA",
                new Point(-80.77640328937902, 35.201116726102015),
                200
        ));
        parkings.add(new Parking(
                "6280d84995e1e9d7f1350141",
                "Harris Teeter",
                "Charlotte",
                "Central Ave",
                "1704",
                "NC 28205",
                "USA",
                new Point(-80.81010062403557, 35.221149613781186),
                150
        ));

        for (Parking parking: parkings) {
            Parking output = ps.add(parking);
            if (output != null) {
                parkingIds.add(output.getId());
            }
        }
    }



    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }


}
