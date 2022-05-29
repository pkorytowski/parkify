package com.parkingsolutions.parkify.document;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("owner")
public class Owner extends BaseUser {

    public Owner(String name, String surname, String email, String password) {
        super(name, surname, email, password);
    }

    public Owner(Owner owner) {
        this(owner.getName(), owner.getSurname(), owner.getEmail(), owner.getPassword());
        setId(owner.getId());
    }
}
