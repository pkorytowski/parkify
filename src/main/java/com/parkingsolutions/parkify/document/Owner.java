package com.parkingsolutions.parkify.document;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("owner")
public class Owner extends BaseUser {

    public Owner(String name, String surname, String email, String password) {
        super(name, surname, email, password);
    }

}
