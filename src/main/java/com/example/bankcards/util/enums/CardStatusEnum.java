package com.example.bankcards.util.enums;

public enum CardStatusEnum {

    ACTIVE("Карта активна."),
    BLOCKED("Карта заблокирована!"),
    EXPIRED("Срок действия карты истёк!");

    private final String label;

    CardStatusEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
