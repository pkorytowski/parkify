package com.parkingsolutions.parkify.service;


import com.parkingsolutions.parkify.bean.AuthorizedAdmin;
import com.parkingsolutions.parkify.bean.AuthorizedUser;
import com.parkingsolutions.parkify.document.Admin;
import com.parkingsolutions.parkify.document.User;
import com.parkingsolutions.parkify.repository.AdminRepository;
import com.parkingsolutions.parkify.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class that handles login operations and JWT generation
 */
@Component
public class LoginService {

    private UserRepository ur;
    private AdminRepository or;

    @Autowired
    public LoginService(UserRepository ur, AdminRepository or) {
        this.ur = ur;
        this.or = or;
    }

    /**
     * Login user
     * @param email
     * @param password
     * @return Instance of authorized user
     * @see AuthorizedUser
     */
    public AuthorizedUser loginUser(String email, String password) {
        System.out.println(email);
        User user = ur.findOneByEmail(email);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                String token = getJWTToken(email, "USER");
                AuthorizedUser authUser = new AuthorizedUser(user, token);
                return authUser;
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect username or password");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");
        }
    }

    /**
     * Login owner
     * @param email
     * @param password
     * @return Instance of authorized user
     * @see AuthorizedUser
     */
    public AuthorizedAdmin loginAdmin(String email, String password) {
        Admin admin = or.findOneByEmail(email);
        if (admin != null) {
            if (admin.getPassword().equals(password)) {
                String token = getJWTToken(email, "OWNER");
                return new AuthorizedAdmin(admin, token);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect username or password");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Owner not exist");
        }
    }

    /**
     * Generate JWT token for given username and role
     * @param username
     * @param role
     * @return JWT token
     */
    private String getJWTToken(String username, String role) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(role);

        String token = Jwts
                .builder()
                .setId("Parkify")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Parkify " + token;
    }
}
