package com.parkingsolutions.parkify.bean;

import com.parkingsolutions.parkify.document.Admin;

/**
 * Class used for sending user data to client app
 */
public class AuthorizedAdmin extends Admin {

    private String token;

    public AuthorizedAdmin(Admin admin, String token) {
        super(admin);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
