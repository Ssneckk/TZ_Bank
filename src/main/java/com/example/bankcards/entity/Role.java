package com.example.bankcards.entity;

import com.example.bankcards.util.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private RoleEnum role_name = RoleEnum.ROLE_USER;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public Role() {
    }

    public int getId() {
        return id;
    }

    public RoleEnum getRole_name() {
        return role_name;
    }

    public void setRole_name(RoleEnum role_name) {
        this.role_name = role_name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Role{" +
                "role_name=" + role_name +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id == role.id && role_name == role.role_name && Objects.equals(user, role.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role_name, user);
    }

    @Override
    public String getAuthority() {
        return getRole_name().toString();
    }
}
