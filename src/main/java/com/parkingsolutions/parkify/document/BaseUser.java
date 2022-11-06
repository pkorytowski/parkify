package com.parkingsolutions.parkify.document;

import com.parkingsolutions.parkify.common.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * Base class for different types of users
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseUser {

    @Id
    private String id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private Role role;

    public BaseUser(String name, String surname, String email, String password, Role role) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
