package com.parkingsolutions.parkify.repository;

import com.parkingsolutions.parkify.document.Owner;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OwnerRepository extends MongoRepository<Owner, String> {
    List<Owner> findAll();
    Owner findOneById(String id);
    Owner findOneByEmail(String email);
    Owner insert(Owner owner);
    Owner save(Owner owner);
}
