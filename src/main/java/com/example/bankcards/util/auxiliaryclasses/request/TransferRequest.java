package com.example.bankcards.util.auxiliaryclasses.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class TransferRequest {

    @NotNull(message = "id карты не может быть 0")
    private int fromCardId;

    @NotNull(message = "id карты не может быть 0")
    private int toCardId;

    @DecimalMin(value = "0.01", message = "Сумма перевода должна быть больше 0")
    private BigDecimal amount;

    public TransferRequest() {
    }

    public TransferRequest(Integer fromCardId, Integer toCardId, BigDecimal amount) {
        this.fromCardId = fromCardId;
        this.toCardId = toCardId;
        this.amount = amount;
    }

    public int getFromCardId() {
        return fromCardId;
    }

    public void setFromCardId(Integer fromCardId) {
        this.fromCardId = fromCardId;
    }

    public int getToCardId() {
        return toCardId;
    }

    public void setToCardId(Integer toCardId) {
        this.toCardId = toCardId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
