package com.parkingsolutions.parkify.service;

import com.parkingsolutions.parkify.document.User;
import com.parkingsolutions.parkify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Class that implements logic for User operations
 */
@Component
public class UserService {

    private UserRepository ur;

    @Autowired
    public UserService(UserRepository ur) {
        this.ur = ur;
    }
/*
    public List<User> getUsers() {
        return ur.findAll();
    }
*/

    /**
     * Add new user with distinct email
     * @param user
     * @return User if created
     * @see User
     */
    public User addUser(User user) {
        User newUser;
        try {
            newUser = ur.insert(user);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "User already exist"
            );
        }
        newUser.setPassword(null);
        return newUser;
    }
/*
    public User getUserById(String id) {
        return ur.findOneById(id);
    }
*/

    /**
     * Get User by email
     * @param email
     * @return User if exists
     * @see User
     */
    public User getUserByEmail(String email) {
        User user = ur.findOneByEmail(email);
        user.setPassword(null);
        return user;
    }

}
