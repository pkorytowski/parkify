package com.parkingsolutions.parkify.controller;

import com.parkingsolutions.parkify.document.User;
import com.parkingsolutions.parkify.service.OwnerService;
import com.parkingsolutions.parkify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller for register new users
 */
@RestController
@RequestMapping("register")
public class RegisterController {
    private UserService us;
    private OwnerService os;

    @Autowired
    public RegisterController(UserService us, OwnerService os) {
        this.us = us;
        this.os = os;
    }

    @PostMapping("user")
    public User registerUser(@RequestBody User user) {
        return us.addUser(user);
    }

    /*
    @PostMapping("owner")
    public Owner registerOwner(@RequestBody Owner owner) {
        return os.addOwner(owner);
    }

     */
}
