package com.example.bankcards.dto;

import com.example.bankcards.entity.CardBlockRequest;

import java.math.BigDecimal;

public class MyRecords {

    public static record simpleCardRecordDTO(Integer id, String cardNumber, String status) {}

    public static record fullCardRecordDTO(Integer id, String cardNumber, String expireDate,
                                           String status, BigDecimal balance) {}
}
