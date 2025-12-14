package com.example.bankcards.util.converters;

import com.example.bankcards.dto.FullCardRecordDTO;
import com.example.bankcards.dto.SimpleCardRecordDTO;
import com.example.bankcards.entity.Card;

/**
 * Интерфейс для конвертации сущности {@link Card} в DTO представления.
 */
public interface CardConverter {

    /**
     * Преобразует {@link Card} в {@link FullCardRecordDTO}.
     *
     * @param card карта для конвертации
     * @return DTO с полной информацией о карте
     */
    FullCardRecordDTO convertToFullCardRecord(Card card);

    /**
     * Преобразует {@link Card} в {@link SimpleCardRecordDTO}.
     *
     * @param card карта для конвертации
     * @return DTO с минимальной информацией о карте (id, номер, статус)
     */
    SimpleCardRecordDTO convertToSimpleCardRecord(Card card);
}
