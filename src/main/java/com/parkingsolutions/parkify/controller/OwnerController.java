package com.parkingsolutions.parkify.controller;

import com.parkingsolutions.parkify.document.Owner;
import com.parkingsolutions.parkify.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController()
@RequestMapping("owner")
public class OwnerController {

    private final OwnerService os;

    @Autowired
    public OwnerController(OwnerService os) {
        this.os = os;
    }

    @GetMapping("/all")
    public @ResponseBody List<Owner> getOwners() {
        return os.getOwners();
    }

    @PostMapping
    public Owner addUser(@RequestBody Owner owner) {
        return os.addOwner(owner);
    }

    @GetMapping
    public @ResponseBody Owner getUser(@RequestParam(name = "id", required = false) String id,
                                      @RequestParam(name = "email", required = false) String email) {
        if (id != null) {
            return os.getOwnerById(id);
        } else if (email != null) {
            return os.getOwnerByEmail(email);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

}
