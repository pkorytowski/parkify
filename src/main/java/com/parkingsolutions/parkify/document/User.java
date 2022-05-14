package com.parkingsolutions.parkify.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
public class User {
    @Id
    private String id;

    private String name;
    private String surname;
    private String email;
    private String password;
    private int thankYou;

    public User(String name, String surname, String email, String password, int thankYou) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.thankYou = thankYou;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getThankYou() {
        return thankYou;
    }

    public void setThankYou(int thankYou) {
        this.thankYou = thankYou;
    }

}
