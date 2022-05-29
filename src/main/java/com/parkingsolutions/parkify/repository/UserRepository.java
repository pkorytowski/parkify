package com.parkingsolutions.parkify.repository;

import com.parkingsolutions.parkify.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    List<User> findAll();
    User findOneByEmail(String email);
    User findOneById(String id);
    User insert(User user);
}
