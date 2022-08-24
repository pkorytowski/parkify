package com.parkingsolutions.parkify.service;

import com.parkingsolutions.parkify.document.Owner;
import com.parkingsolutions.parkify.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Class that implements logic for owner operations
 */
@Component
public class OwnerService {

    private OwnerRepository or;

    @Autowired
    public OwnerService(OwnerRepository or) {
        this.or = or;
    }

    /**
     * Get all owners
     * @return list of all owners in db
     * @see Owner
     */
    public List<Owner> getOwners() {
        return or.findAll();
    }

    /**
     * Add new owner with distinct email
     * @param owner
     * @return Owner instance if created
     */
    public Owner addOwner(Owner owner) {
        Owner newOwner;
        try {
            newOwner = or.insert(owner);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "User already exist"
            );
        }
        return newOwner;
    }

    /**
     * Get owner by id
     * @param id
     * @return Owner is exists
     * @see Owner
     */
    public Owner getOwnerById(String id) {
        return or.findOneById(id);
    }

    /**
     * Get owner by email
     * @param email
     * @return Owner if exists
     * @see Owner
     */
    public Owner getOwnerByEmail(String email) {
        return or.findOneByEmail(email);
    }


}
