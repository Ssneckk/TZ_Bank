package com.example.bankcards.service.card;

/**
 * Сервис для удаления карт пользователей.
 * Предоставляет метод для логического удаления карты по идентификатору
 */
public interface CardDeletionService {

    /**
     * Логически удаляет карту по её идентификатору
     *
     * @param cardId идентификатор карты
     */
    void delete(Integer cardId);
}
