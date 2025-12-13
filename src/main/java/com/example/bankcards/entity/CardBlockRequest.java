package com.example.bankcards.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "card_block_requests")
public class CardBlockRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "card_id")
    private int cardId;

    @Column(name = "requested_user_id")
    private int requestedUserId;

    @Column(name = "requested_at")
    private LocalDate requestedAt;

    public CardBlockRequest() {
    }

    public CardBlockRequest(int cardId, int requestedUserId, LocalDate requestedAt) {
        this.cardId = cardId;
        this.requestedUserId = requestedUserId;
        this.requestedAt = requestedAt;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public int getRequestedUserId() {
        return requestedUserId;
    }

    public void setRequestedUserId(int requestedUserId) {
        this.requestedUserId = requestedUserId;
    }

    public LocalDate getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDate requestedAt) {
        this.requestedAt = requestedAt;
    }
}
