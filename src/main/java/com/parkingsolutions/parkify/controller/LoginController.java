package com.parkingsolutions.parkify.controller;


import com.parkingsolutions.parkify.bean.AuthorizedAdmin;
import com.parkingsolutions.parkify.bean.AuthorizedUser;
import com.parkingsolutions.parkify.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Rest controller for login activities
 */
@RestController
@RequestMapping("login")
public class LoginController {

    private LoginService ls;

    @Autowired
    public LoginController(LoginService ls) {
        this.ls = ls;
    }

    @PostMapping("user")
    public AuthorizedUser loginUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        return ls.loginUser(email, password);
    }

    @PostMapping("owner")
    public AuthorizedAdmin loginOwner(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        return ls.loginOwner(email, password);
    }



}
