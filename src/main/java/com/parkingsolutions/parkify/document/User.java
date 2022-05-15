package com.parkingsolutions.parkify.document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("user")
public class User extends BaseUser{

    private int rank;

    public User() {}

    public User(String name, String surname, String email, String password, int rank) {
        super(name, surname, email, password);
        this.rank = rank;
    }

    @JsonCreator
    public User(@JsonProperty("name") String name,
                @JsonProperty("surname") String surname,
                @JsonProperty("email") String email,
                @JsonProperty("password") String password) {
        super(name, surname, email, password);
        this.rank = 0;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

}
