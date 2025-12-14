package com.example.bankcards.service.card;

import java.util.Map;

/**
 * Сервис для управления статусами карт.
 * Предоставляет методы для блокировки, активации и истечения срока действия карт
 */
public interface CardStatusService {

    /**
     * Устанавливает статус "истекший" для всех карт, срок действия которых завершился.
     */
    void setCardsExpired();

    /**
     * Блокирует карту по идентификатору.
     *
     * @param cardId идентификатор карты
     * @return карта заблокирована, информация в виде ключ-значение
     */
    Map<String, String> blockCard(Integer cardId);

    /**
     * Активирует карту по идентификатору.
     *
     * @param cardId идентификатор карты
     * @return карта активирована, информация в виде ключ-значение
     */
    Map<String, String> activateCard(Integer cardId);
}
