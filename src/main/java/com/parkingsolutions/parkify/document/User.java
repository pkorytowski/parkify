package com.parkingsolutions.parkify.document;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("user")
public class User extends BaseUser{

    private int rank;

    public User(String name, String surname, String email, String password, int rank) {
        super(name, surname, email, password);
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

}
