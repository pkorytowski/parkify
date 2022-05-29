package com.parkingsolutions.parkify.service;

import com.parkingsolutions.parkify.document.Owner;
import com.parkingsolutions.parkify.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class OwnerService {

    private OwnerRepository or;

    @Autowired
    public OwnerService(OwnerRepository or) {
        this.or = or;
    }

    public List<Owner> getOwners() {
        return or.findAll();
    }

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

    public Owner getOwnerById(String id) {
        return or.findOneById(id);
    }

    public Owner getOwnerByEmail(String email) {
        return or.findOneByEmail(email);
    }


}
