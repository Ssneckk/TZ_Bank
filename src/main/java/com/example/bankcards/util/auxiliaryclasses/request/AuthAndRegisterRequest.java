package com.example.bankcards.util.auxiliaryclasses.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class AuthAndRegisterRequest {

    @Email(message = "Неверный формат email")
    @NotEmpty(message = "Email не может быть пустым")
    private String email;

    @NotEmpty(message = "Пароль не должен быть пустым")
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AuthAndRegisterRequest() {
    }
}
