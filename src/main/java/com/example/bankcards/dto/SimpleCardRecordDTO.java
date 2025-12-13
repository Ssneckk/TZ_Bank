package com.example.bankcards.dto;

/**
 * DTO, содержащий краткую информацию о карте.
 * Поля:
 * <ul>
 *     <li>{@code id} - идентификатор карты;</li>
 *     <li>{@code meshCardNumber} - зашифрованный 16-значный номер карты;</li>
 *     <li>{@code status} - статус карты (например: заблокирована, активна, удалена).</li>
 * </ul>
 */
public record SimpleCardRecordDTO(
        Integer id,
        String meshCardNumber,
        String status) { }
