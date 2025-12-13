package com.example.bankcards.controller;

import com.example.bankcards.dto.FullCardRecordDTO;
import com.example.bankcards.dto.SimpleCardRecordDTO;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.service.card.*;
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
        return ResponseEntity.ok(cardCreationService.createCard(userId));
    }

    /**
     * Получает список карт пользователей
     * @param pageable - параметры пагинации и сортировки
     * @return страница {@link SimpleCardRecordDTO} DTO с краткими данными карт
     */
    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<SimpleCardRecordDTO>> getCards(Pageable pageable) {
        return ResponseEntity.ok(cardService.getCards(pageable));
    }

    /**
     * Блокирует карту с указанным идентификатором
     * @param cardId - идентификатор карты
     * @return {@link Map} Сообщение с результатом операции
     */
    @PatchMapping("/{cardId}/block")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> blockCard(@PathVariable Integer cardId) {
        return ResponseEntity.ok(cardStatusService.blockCard(cardId));
    }

    /**
     * Активирует карту с указанным идентификатором
     * @param cardId - идентификатор карты
     * @return {@link Map} Сообщение с результатом операции
     */
    @PatchMapping("/{cardId}/activate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> activateCard(@PathVariable Integer cardId) {
        return ResponseEntity.ok(cardStatusService.activateCard(cardId));
    }

    /**
     * Удаляет карту с указанным идентификатором
     * @param cardId - идентификатор карты
     * @return {@link String} Сообщение с результатом операции
     */
    @DeleteMapping("/{cardId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteCard(@PathVariable Integer cardId) {
        cardDeletionService.delete(cardId);

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
        return ResponseEntity.ok(cardBlockRequestService.getRequests(pageable));
    }

    /**
     * Блокирует карту через запрос при помощи идентификатора запроса
     * @param requestId - идентификатор запроса на блокировку
     * @return {@link Map} Сообщение с результатом операции
     */
    @DeleteMapping("/block-requests/{requestId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> blockCardByRequest(@PathVariable Integer requestId) {
        return ResponseEntity.ok(cardBlockRequestService.blockByRequest(requestId));
    }
}
