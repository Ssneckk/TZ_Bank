package com.example.bankcards.dto;

import com.example.bankcards.util.enums.RoleEnum;

/**
 * DTO, содержащий роль пользователя.
 * <p>Поле: {@link RoleEnum } роль пользователя админ/пользователь.</p>
 */
public class RoleDTO {

    private RoleEnum role_name;

    public RoleEnum getRole_name() {
        return role_name;
    }

    public void setRole_name(RoleEnum role_name) {
        this.role_name = role_name;
    }

    @Override
    public String toString() {
        return "RoleDTO{" +
                "role_name=" + role_name +
                '}';
    }
}
