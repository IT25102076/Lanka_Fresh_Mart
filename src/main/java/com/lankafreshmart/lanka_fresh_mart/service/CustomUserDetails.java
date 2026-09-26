package com.lankafreshmart.lanka_fresh_mart.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {

    private final String firstName;
    private final String lastName;
    private final String fullName;
    private final String address;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String firstName, String lastName, String address) {
        super(username, password, authorities);
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAddress() {
        return address;
    }
}
