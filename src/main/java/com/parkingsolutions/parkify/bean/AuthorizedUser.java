package com.parkingsolutions.parkify.bean;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.parkingsolutions.parkify.document.User;

import java.io.Serializable;

public class AuthorizedUser extends User implements Serializable {

    private String token;

    @JsonCreator
    public AuthorizedUser(User user, @JsonProperty("token") String token) {
        super(user);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
