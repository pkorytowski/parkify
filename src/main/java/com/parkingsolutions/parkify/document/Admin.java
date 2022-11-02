package com.parkingsolutions.parkify.document;

import com.parkingsolutions.parkify.common.Role;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Class created for the parkings' owners. Owner functionality non implemented.
 */
@Document("owner")
public class Admin extends BaseUser {

    public Admin(String name, String surname, String email, String password) {
        super(name, surname, email, password, Role.ADMIN);
    }

    public Admin(Admin admin) {
        this(admin.getName(), admin.getSurname(), admin.getEmail(), admin.getPassword());
        setId(admin.getId());
    }
}
