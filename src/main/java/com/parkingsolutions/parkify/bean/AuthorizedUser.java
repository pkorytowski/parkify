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


    public AuthorizedUser(User user, String token) {
        super(user);
        this.token = token;
    }
    @JsonCreator
    public AuthorizedUser(@JsonProperty("name") String name,
                          @JsonProperty("surname") String surname,
                          @JsonProperty("email") String email,
                          @JsonProperty("password") String password,
                          @JsonProperty("token") String token) {
        super(name, surname, email, password);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
