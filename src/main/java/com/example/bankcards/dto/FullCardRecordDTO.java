package com.example.bankcards.dto;

import java.math.BigDecimal;

/**
 * DTO, содержащий полную информацию о карте.
 * Поля:
 * <ul>
 *     <li>{@code id} - идентификатор карты;</li>
 *     <li>{@code meshCardNumber} - зашифрованный 16-значный номер карты;</li>
 *     <li>{@code formattedDate} - дата (годна до) в формате dd/MM;</li>
 *     <li>{@code status} - статус карты (например: заблокирована, активна, удалена);</li>
 *     <li>{@code balance} - баланс карты (например: 0.00).</li>
 * </ul>
 */
public record FullCardRecordDTO(
        Integer id,
        String meshCardNumber,
        String formattedDate,
        String status,
        BigDecimal balance) {
}
