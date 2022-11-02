package com.parkingsolutions.parkify.service;

import com.parkingsolutions.parkify.document.Admin;
import com.parkingsolutions.parkify.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Class that implements logic for owner operations
 */
@Component
public class AdminService {

    private AdminRepository or;

    @Autowired
    public AdminService(AdminRepository or) {
        this.or = or;
    }

    /**
     * Get all owners
     * @return list of all owners in db
     * @see Admin
     */
    public List<Admin> getAdmins() {
        return or.findAll();
    }

    /**
     * Add new owner with distinct email
     * @param admin
     * @return Owner instance if created
     */
    public Admin addAdmin(Admin admin) {
        Admin newAdmin;
        try {
            newAdmin = or.insert(admin);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "User already exist"
            );
        }
        return newAdmin;
    }

    /**
     * Get owner by id
     * @param id
     * @return Owner is exists
     * @see Admin
     */
    public Admin getAdminById(String id) {
        return or.findOneById(id);
    }

    /**
     * Get owner by email
     * @param email
     * @return Owner if exists
     * @see Admin
     */
    public Admin getAdminByEmail(String email) {
        return or.findOneByEmail(email);
    }


}
