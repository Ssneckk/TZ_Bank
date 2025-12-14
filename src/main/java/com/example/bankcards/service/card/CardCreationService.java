package com.example.bankcards.service.card;

import com.example.bankcards.dto.FullCardRecordDTO;

/**
 * Сервис для создания новых карт пользователям.
 * Предоставляет метод для генерации и сохранения карты.
 */
public interface CardCreationService {

    /**
     * Создает карту для пользователя по идентификатору.
     * @param id идентификатор пользователя
     * @return {@link FullCardRecordDTO} с полной информацией о созданной карте
     */
    FullCardRecordDTO createCard(Integer id);
}
