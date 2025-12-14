package com.example.bankcards.service.card;

import com.example.bankcards.dto.FullCardRecordDTO;
import com.example.bankcards.dto.SimpleCardRecordDTO;
import com.example.bankcards.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Сервис для работы с картами.
 * Предоставляет методы для получения, поиска и доступа к информации о картах пользователей.
 */
public interface CardService {

    /**
     * Получает все карты с пагинацией.
     *
     * @param pageable параметры пагинации
     * @return страница DTO с краткой информацией о картах
     */
    Page<SimpleCardRecordDTO> getCards(Pageable pageable);

    /**
     * Получает карты текущего пользователя с пагинацией.
     *
     * @param pageable параметры пагинации
     * @return страница DTO с краткой информацией о картах пользователя
     */
    Page<SimpleCardRecordDTO> getUsersCards(Pageable pageable);

    /**
     * Получает карту текущего пользователя по идентификатору.
     *
     * @param cardId идентификатор карты
     * @return DTO с полной информацией о карте
     */
    FullCardRecordDTO getUsersCard(Integer cardId);

    /**
     * Находит карту по идентификатору.
     *
     * @param cardId идентификатор карты
     * @return сущность карты
     */
    Card findCardById(Integer cardId);
}

