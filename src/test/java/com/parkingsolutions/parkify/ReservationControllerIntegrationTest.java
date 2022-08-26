package com.parkingsolutions.parkify;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.parkingsolutions.parkify.bean.AuthorizedUser;
import com.parkingsolutions.parkify.common.ReservationStatus;
import com.parkingsolutions.parkify.document.Parking;
import com.parkingsolutions.parkify.document.Reservation;
import com.parkingsolutions.parkify.service.ParkingService;
import com.parkingsolutions.parkify.service.ReservationService;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.geo.Point;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@ActiveProfiles(value = "test")
@SpringBootTest(classes = {ParkifyApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ContextConfiguration
public class ReservationControllerIntegrationTest {

    @LocalServerPort
    private int port;

    HttpHeaders headers = new HttpHeaders();

    TestRestTemplate restTemplate = new TestRestTemplate();

    private List<String> parkingIds = new ArrayList<>();
    private final String SECRET = "mySecretKey";
    private final String PREFIX = "Parkify ";

    private final ReservationService rs;
    private final ParkingService ps;

    @Autowired
    public ReservationControllerIntegrationTest(ReservationService rs, ParkingService ps) {
        this.rs = rs;
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

        AuthorizedUser user = null;

        user = objectMapper.readValue(response.getBody(), AuthorizedUser.class);
        headers.add("Authorization", user.getToken());
        setUpParkingsInDb();
    }

    public void setUpParkingsInDb() {
        List<Parking> parkings = new ArrayList<>();
            parkings.add(new Parking(
                    "6280d84995e1e9d7f1350141",
                    "Test Parking",
                    "Krakow",
                    "Al. Mickiewicza",
                    "22",
                    "22-222",
                    "Poland",
                    new Point(19.914240, 50.066330),
                    10
            ));
        parkings.add(new Parking(
                "6280d84995e1e9d7f1350141",
                "Test Parking",
                "Krakow",
                "Al. Mickiewicza",
                "22",
                "22-222",
                "Poland",
                new Point(19.914240, 50.066330),
                0
        ));

        for (Parking parking: parkings) {
            Parking output = ps.add(parking);
            if (output != null) {
                parkingIds.add(output.getId());
            }
        }
    }

    @Test
    public void addNewReservation() throws JsonProcessingException {
        Map<String, String> data = new HashMap<>();
        data.put("parkingId", parkingIds.get(0));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String dataStr = null;
        try {
            dataStr = objectMapper.writeValueAsString(data);
            System.out.println(dataStr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //restTemplate.
        HttpEntity<String> entity = new HttpEntity<>(dataStr, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/reservation"),
                HttpMethod.POST, entity, String.class);
        //Reservation reservation = response.getBody();

        String user = Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(headers.getFirst("Authorization").replace(PREFIX, ""))
                .getBody()
                .getSubject();

        Reservation reservation = null;

        reservation = objectMapper.readValue(response.getBody(), Reservation.class);

        assert(Objects.requireNonNull(reservation).getReservationStatus().equals(ReservationStatus.RESERVED));
        assert(reservation.getUserId().equals(user));

    }

    @Test
    public void addTwoReservations() throws JsonProcessingException {
        Map<String, String> data = new HashMap<>();
        data.put("parkingId", parkingIds.get(0));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String dataStr = null;
        try {
            dataStr = objectMapper.writeValueAsString(data);
            System.out.println(dataStr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //restTemplate.
        HttpEntity<String> entity = new HttpEntity<>(dataStr, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/reservation"),
                HttpMethod.POST, entity, String.class);
        //Reservation reservation = response.getBody();

        String user = Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(headers.getFirst("Authorization").replace(PREFIX, ""))
                .getBody()
                .getSubject();

        Reservation reservation = null;

        reservation = objectMapper.readValue(response.getBody(), Reservation.class);

        assertEquals(Objects.requireNonNull(reservation).getReservationStatus(), ReservationStatus.RESERVED);
        assertEquals(reservation.getUserId(), user);

        response = restTemplate.exchange(
                createURLWithPort("/reservation"),
                HttpMethod.POST, entity, String.class);
        //Reservation reservation = response.getBody();

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

    }

    @Test
    public void addToFullParking() {
        Map<String, String> data = new HashMap<>();
        data.put("parkingId", parkingIds.get(1));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String dataStr = null;
        try {
            dataStr = objectMapper.writeValueAsString(data);
            System.out.println(dataStr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //restTemplate.
        HttpEntity<String> entity = new HttpEntity<>(dataStr, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/reservation"),
                HttpMethod.POST, entity, String.class);
        //Reservation reservation = response.getBody();

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

    }

    @AfterEach
    public void deleteAllReservation() {
        String token = headers.getFirst("Authorization");
        if (token != null)
            rs.deleteAllReservations(token);
    }

    @AfterAll
    public void deleteAllParkings() {
        for(String id: parkingIds) {
            ps.deleteOneById(id);
        }
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}
