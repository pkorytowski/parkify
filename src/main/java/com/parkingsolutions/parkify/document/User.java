package com.parkingsolutions.parkify.document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.parkingsolutions.parkify.common.Role;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Class for ordinary user without special permission
 */
@Document("user")
public class User extends BaseUser{

    /**
     * If user ends occupation of parking spot before declared time the rank is increased, otherwise is decreased.
     */
    private int rank;

    public User() {}

    public User(User user) {
        this(user.getName(), user.getSurname(), user.getEmail(), user.getPassword(), user.getRank());
        setId(user.getId());
    }

    public User(String name, String surname, String email, String password, int rank) {
        super(name, surname, email, password, Role.USER);
        this.rank = rank;
    }


    /**
     * Constructor used for creating objects from JSON (e.g. from client app).
     * @param name name of the user
     * @param  surname of the user
     * @param email must be unique within database
     * @param password minimum 8 letters
     */
    @JsonCreator
    public User(@JsonProperty("name") String name,
                @JsonProperty("surname") String surname,
                @JsonProperty("email") String email,
                @JsonProperty("password") String password) {
        super(name, surname, email, password, Role.USER);
        this.rank = 0;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void increaseRank() {
        rank++;
    }

    public void decreaseRank() {
        rank--;
    }
}
