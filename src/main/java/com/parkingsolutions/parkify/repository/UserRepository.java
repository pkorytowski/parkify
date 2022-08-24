package com.parkingsolutions.parkify.repository;

import com.parkingsolutions.parkify.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    /**
     * Get all users from DB
     * @return List of all users
     * @see User
     */
    List<User> findAll();

    /**
     * Get user by email
     * @param email
     * @return User if exists
     * @see User
     */
    User findOneByEmail(String email);

    /**
     * Get user by Id
     * @param id
     * @return User if exists
     * @see User
     */
    User findOneById(String id);

    /**
     * Save new user if not exists
     * @param user
     * @return Saved user
     * @see User
     */
    User insert(User user);
}
