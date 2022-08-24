package com.parkingsolutions.parkify.controller;

import com.parkingsolutions.parkify.document.User;
import com.parkingsolutions.parkify.service.UserService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService us;

    private String SECRET = "mySecretKey";
    private String PREFIX = "Parkify ";
    @Autowired
    public UserController(UserService us) {
        this.us = us;
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return us.addUser(user);
    }

    @GetMapping
    public @ResponseBody User getOne(@RequestHeader (name = "Authorization") String token) {
        String user = Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token.replace(PREFIX, ""))
                .getBody()
                .getSubject();

        return us.getUserByEmail(user);
    }

}
