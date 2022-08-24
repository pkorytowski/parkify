package com.parkingsolutions.parkify.bean;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.parkingsolutions.parkify.document.User;

import java.io.Serializable;

/**
 * Class used for sending user data to client app
 */
public class AuthorizedUser extends User implements Serializable {

    /**
     * The JWT token
     */
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
