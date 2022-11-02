package com.parkingsolutions.parkify.repository;

import com.parkingsolutions.parkify.document.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring data Mongo Repository for Owner collection.
 */
@Repository
public interface AdminRepository extends MongoRepository<Admin, String> {
    List<Admin> findAll();
    Admin findOneById(String id);
    Admin findOneByEmail(String email);
    Admin insert(Admin admin);
    Admin save(Admin admin);
}
