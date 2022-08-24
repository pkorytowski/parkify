package com.parkingsolutions.parkify.bean;

import com.parkingsolutions.parkify.document.Owner;
import com.parkingsolutions.parkify.document.User;

/**
 * Class used for sending user data to client app
 */
public class AuthorizedOwner extends Owner {

    private String token;

    public AuthorizedOwner(Owner owner, String token) {
        super(owner);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
