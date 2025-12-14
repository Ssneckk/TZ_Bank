package com.example.bankcards.service.card;

import com.example.bankcards.entity.CardBlockRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Сервис для работы с запросами на блокировку карт.
 * Предоставляет методы для создания запросов, блокировки карт и получения всех запросов.
 */
public interface CardBlockRequestService {

    /**
     * Создает запрос на блокировку карты текущего пользователя.
     * @param cardId идентификатор карты
     * @return созданный объект {@link CardBlockRequest}
     */
    CardBlockRequest makeRequest(Integer cardId);

    /**
     * Блокирует карту по существующему запросу на блокировку.
     * @param cardBlockRequestId идентификатор запроса на блокировку
     * @return карта заблокирована, информация в виде Map
     */
    Map<String,String> blockByRequest(Integer cardBlockRequestId);

    /**
     * Получает все запросы на блокировку карт с пагинацией.
     * @param pageable параметры пагинации
     * @return страница запросов {@link CardBlockRequest}
     */
    Page<CardBlockRequest> getRequests(Pageable pageable);
}
