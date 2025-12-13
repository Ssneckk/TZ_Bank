package com.example.bankcards.entity;

import com.example.bankcards.util.enums.CardStatusEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Номер карты не должен быть пустым")
    private String card_number_encrypted;

    @NotEmpty(message = "Последние 4 цифры не должны быть пустыми")
    private String card_number_last4;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column(name = "expire_date")
    private LocalDate expireDate;

    @Enumerated(EnumType.STRING)
    private CardStatusEnum status=CardStatusEnum.ACTIVE;

    @Column(name = "balance", precision = 12, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    public Card() {
    }

    public Card(LocalDate expireDate, User user, String card_number_encrypted, String card_number_last4) {
        this.user = user;
        this.card_number_encrypted = card_number_encrypted;
        this.card_number_last4 = card_number_last4;
        this.expireDate = expireDate;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public CardStatusEnum getStatus() {
        return status;
    }

    public void setStatus(CardStatusEnum status) {
        this.status = status;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCard_number_encrypted() {
        return card_number_encrypted;
    }

    public String getCard_number_last4() {
        return card_number_last4;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return id == card.id && Objects.equals(card_number_encrypted, card.card_number_encrypted) && Objects.equals(user, card.user) && Objects.equals(expireDate, card.expireDate) && status == card.status && Objects.equals(balance, card.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, card_number_encrypted, user, expireDate, status, balance);
    }
}
