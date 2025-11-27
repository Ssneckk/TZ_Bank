package com.example.bankcards.security;

import com.example.bankcards.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetailsImpls implements UserDetails {

    private User user;

    public UserDetailsImpls(User user) {
        this.user = user;
    }

    public UserDetailsImpls() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    public int getId(){
        return user.getId();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.getBlocked();
    }
}
