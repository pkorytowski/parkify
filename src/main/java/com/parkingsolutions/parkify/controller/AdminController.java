package com.parkingsolutions.parkify.controller;

import com.parkingsolutions.parkify.document.Admin;
import com.parkingsolutions.parkify.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Rest controller for activities related to owner profile.
 * Currently, not in use.
 */
@RestController()
@RequestMapping("owner")
public class AdminController {

    private final AdminService os;

    @Autowired
    public AdminController(AdminService os) {
        this.os = os;
    }

    @GetMapping("/all")
    public @ResponseBody List<Admin> getOwners() {
        return os.getOwners();
    }

    @PostMapping
    public Admin addUser(@RequestBody Admin admin) {
        return os.addOwner(admin);
    }

    @GetMapping
    public @ResponseBody Admin getUser(@RequestParam(name = "id", required = false) String id,
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
