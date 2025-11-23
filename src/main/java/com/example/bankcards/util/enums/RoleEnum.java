package com.example.bankcards.util.enums;

public enum RoleEnum {

    ROLE_ADMIN("Админ"),
    ROLE_USER("Пользователь");

    private final String label;

    RoleEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
