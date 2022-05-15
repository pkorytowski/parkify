package com.parkingsolutions.parkify.controller;

import com.parkingsolutions.parkify.document.User;
import com.parkingsolutions.parkify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService us;

    @Autowired
    public UserController(UserService us) {
        this.us = us;
    }

    @GetMapping("/all")
    public @ResponseBody List<User> getAll() {
        return us.getUsers();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return us.addUser(user);
    }

    @GetMapping
    public @ResponseBody User getOne(@RequestParam(name = "id", required = false) String id,
                                     @RequestParam(name = "email", required = false) String email) {
        if (id != null) {
            return us.getUserById(id);
        } else if (email != null) {
            return us.getUserByEmail(email);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

}
