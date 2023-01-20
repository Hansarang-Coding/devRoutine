package com.likelion.devroutine.auth.dto;

import com.likelion.devroutine.auth.domain.User;

import java.io.Serializable;

public class SessionUser implements Serializable {
    private final String name;
    private final String email;
    private final String picture;

    public SessionUser(User user){
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getEmail();
    }
}
