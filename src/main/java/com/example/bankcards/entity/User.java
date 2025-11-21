package com.example.bankcards.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "\"user\"")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Email(message = "Неверный формат email")
    @NotEmpty(message = "Email не может быть пустым")
    @Column(unique = true , nullable = false)
    private String email;

    @NotEmpty(message = "Пароль не должен быть пустым")
    private String password;

    @OneToMany(mappedBy = "user",orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Role> roles;
}
