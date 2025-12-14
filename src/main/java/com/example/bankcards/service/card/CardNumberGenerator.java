package com.example.bankcards.service.card;

/**
 * Сервис для генерации номеров карт.
 * Предоставляет метод для создания уникального номера карты
 */
public interface CardNumberGenerator {

    /**
     * Генерирует уникальный номер карты.
     *
     * @return сгенерированный номер карты в виде строки
     */
    String generateCardNumber();
}

