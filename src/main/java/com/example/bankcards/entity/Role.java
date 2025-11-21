package com.example.bankcards.entity;

import com.example.bankcards.util.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "role")
@Data
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Enumerated(EnumType.STRING)
    private RoleEnum role_name = RoleEnum.USER;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public String getAuthority() {
        return getRole_name().toString();
    }
}
