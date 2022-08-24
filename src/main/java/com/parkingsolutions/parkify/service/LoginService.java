package com.parkingsolutions.parkify.service;


import com.parkingsolutions.parkify.bean.AuthorizedOwner;
import com.parkingsolutions.parkify.bean.AuthorizedUser;
import com.parkingsolutions.parkify.document.Owner;
import com.parkingsolutions.parkify.document.User;
import com.parkingsolutions.parkify.repository.OwnerRepository;
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
    private OwnerRepository or;

    @Autowired
    public LoginService(UserRepository ur, OwnerRepository or) {
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
    public AuthorizedOwner loginOwner(String email, String password) {
        Owner owner = or.findOneByEmail(email);
        if (owner != null) {
            if (owner.getPassword().equals(password)) {
                String token = getJWTToken(email, "OWNER");
                AuthorizedOwner authOwner = new AuthorizedOwner(owner, token);
                return authOwner;
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
