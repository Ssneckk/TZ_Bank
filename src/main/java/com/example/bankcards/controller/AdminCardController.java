package com.example.bankcards.controller;

import com.example.bankcards.dto.FullCardRecordDTO;
import com.example.bankcards.dto.SimpleCardRecordDTO;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.service.card.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST-контроллер с административными операциями управления банковскими картами.
 * <p>
 * Предоставляет функциональность для:
 * <ul>
 *   <li>создания карт для пользователей;</li>
 *   <li>получения списка карт пользователей;</li>
 *   <li>блокировки, активации и удаления карт по идентификатору;</li>
 *   <li>обработки запросов на блокировку карт.</li>
 * </ul>
 * Доступен только пользователям с ролью ADMIN.
 */
@RestController
@RequestMapping("/api/admin/cards")
public class AdminCardController {

    private final CardCreationService cardCreationService;
    private final CardService cardService;
    private final CardStatusService cardStatusService;
    private final CardDeletionService cardDeletionService;
    private final CardBlockRequestService cardBlockRequestService;
    private static final Logger log = LoggerFactory.getLogger(AdminCardController.class);

    @Autowired
    public AdminCardController(CardCreationService cardCreationService, CardService cardService, CardStatusService cardStatusService, CardDeletionService cardDeletionService, CardBlockRequestService cardBlockRequestService) {
        this.cardCreationService = cardCreationService;
        this.cardService = cardService;
        this.cardStatusService = cardStatusService;
        this.cardDeletionService = cardDeletionService;
        this.cardBlockRequestService = cardBlockRequestService;
    }

    /**
     * Создает банковскую карту для указанного пользователя.
     *
     * @param userId идентификатор пользователя — владельца карты
     * @return {@link FullCardRecordDTO} DTO с полными данными созданной карты
     */
    @PostMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<FullCardRecordDTO> createCard(@PathVariable Integer userId) {
        log.info("ADMIN запросил создание карты для пользователя id={}", userId);

        FullCardRecordDTO card = cardCreationService.createCard(userId);
        log.debug("Создана карта: {}", card);

        return ResponseEntity.ok(card);
    }

    /**
     * Получает список карт пользователей
     * @param pageable - параметры пагинации и сортировки
     * @return страница {@link SimpleCardRecordDTO} DTO с краткими данными карт
     */
    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<SimpleCardRecordDTO>> getCards(Pageable pageable) {
        log.info("ADMIN запросил список карт. Страница={}, размер={}", pageable.getPageNumber(), pageable.getPageSize());

        Page<SimpleCardRecordDTO> cards = cardService.getCards(pageable);
        log.debug("Количество карт возвращено: {}", cards.getContent().size());

        return ResponseEntity.ok(cards);
    }

    /**
     * Блокирует карту с указанным идентификатором
     * @param cardId - идентификатор карты
     * @return {@link Map} Сообщение с результатом операции
     */
    @PatchMapping("/{cardId}/block")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> blockCard(@PathVariable Integer cardId) {
        log.info("ADMIN запросил блокировку карты id={}", cardId);

        Map<String, String> result = cardStatusService.blockCard(cardId);
        log.debug("Результат блокировки карты id={}: {}", cardId, result);

        return ResponseEntity.ok(result);
    }

    /**
     * Активирует карту с указанным идентификатором
     * @param cardId - идентификатор карты
     * @return {@link Map} Сообщение с результатом операции
     */
    @PatchMapping("/{cardId}/activate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> activateCard(@PathVariable Integer cardId) {
        log.info("ADMIN запросил активацию карты id={}", cardId);

        Map<String, String> result = cardStatusService.activateCard(cardId);
        log.debug("Результат активации карты id={}: {}", cardId, result);

        return ResponseEntity.ok(result);
    }

    /**
     * Удаляет карту с указанным идентификатором
     * @param cardId - идентификатор карты
     * @return {@link String} Сообщение с результатом операции
     */
    @DeleteMapping("/{cardId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteCard(@PathVariable Integer cardId) {
        log.info("ADMIN запросил удаление карты id={}", cardId);

        cardDeletionService.delete(cardId);
        log.debug("Карта id={} удалена", cardId);

        return ResponseEntity.noContent().build();
    }

    /**
     * Получает запросы на блокировку
     * @param pageable - параметры пагинации и сортировки
     * @return Страница {@link CardBlockRequest} с информацией о запросах на блокировку
     */
    @GetMapping("/block-requests")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<CardBlockRequest>> getRequests(Pageable pageable) {
        log.info("ADMIN запросил список запросов на блокировку карт. Страница={}, размер={}", pageable.getPageNumber(), pageable.getPageSize());

        Page<CardBlockRequest> requests = cardBlockRequestService.getRequests(pageable);
        log.debug("Количество запросов возвращено: {}", requests.getContent().size());

        return ResponseEntity.ok(requests);
    }

    /**
     * Блокирует карту через запрос при помощи идентификатора запроса
     * @param requestId - идентификатор запроса на блокировку
     * @return {@link Map} Сообщение с результатом операции
     */
    @DeleteMapping("/block-requests/{requestId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> blockCardByRequest(@PathVariable Integer requestId) {
        log.info("ADMIN запросил блокировку карты по запросу id={}", requestId);

        Map<String, String> result = cardBlockRequestService.blockByRequest(requestId);
        log.debug("Результат блокировки по запросу id={}: {}", requestId, result);

        return ResponseEntity.ok(result);
    }
}
