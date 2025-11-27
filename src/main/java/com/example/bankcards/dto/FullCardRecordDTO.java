package com.example.bankcards.dto;

import java.math.BigDecimal;

public record FullCardRecordDTO(Integer id, String meshCardNumber, String formattedDate, String status, BigDecimal balance) {
}
