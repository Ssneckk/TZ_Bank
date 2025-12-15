package com.example.bankcards.controller;

import com.example.bankcards.dto.FullCardRecordDTO;
import com.example.bankcards.dto.SimpleCardRecordDTO;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.service.card.CardBlockRequestService;
import com.example.bankcards.service.card.CardService;
import com.example.bankcards.service.transfer.TransferService;
import com.example.bankcards.util.auxiliaryclasses.request.TransferRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST-контроллер доступный для аутентифицированных пользователей
 * <p>Предоставляет возможности для:</p>
 * <ul>
 *     <li>Получения своих карт;</li>
 *     <li>Получения полной информации своей карты;</li>
 *     <li>Запросить блокировку карты;</li>
 *     <li>Сделать перевод между своими счетами.</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/cards")
public class CardController {

    public final CardService cardService;
    public final TransferService transferService;
    public final CardBlockRequestService cardBlockRequestService;
    private static final Logger log = LoggerFactory.getLogger(CardController.class);

    @Autowired
    public CardController(CardService cardService, TransferService transferService, CardBlockRequestService cardBlockRequestService) {
        this.cardService = cardService;
        this.transferService = transferService;
        this.cardBlockRequestService = cardBlockRequestService;
    }

    /**
     * Возвращает страницу карт текущего пользователя с возможностью пагинации и сортировки
     * @param pageable - параметры пагинации и сортировки
     * @return страница {@link SimpleCardRecordDTO} DTO с краткой информацией карты
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Page<SimpleCardRecordDTO>> getUsersCards(@PageableDefault(size = 10) Pageable pageable) {
        log.info("CardController: Пользователь запросил свои карты. Страница={}, размер={}", pageable.getPageNumber(), pageable.getPageSize());

        Page<SimpleCardRecordDTO> cards = cardService.getUsersCards(pageable);
        log.debug("CardController: Количество карт на странице: {}", cards.getContent().size());

        return ResponseEntity.ok(cards);
    }

    /**
     * Возвращает полную информацию о карте текущего пользователя по идентификатору
     * @param cardId - идентификатор карты
     * @return {@link FullCardRecordDTO} DTO с полной информацией о карте
     */
    @GetMapping("/{cardId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<FullCardRecordDTO> getCard(@PathVariable Integer cardId) {
        log.info("CardController: Пользователь запросил полную информацию карты id={}", cardId);

        FullCardRecordDTO card = cardService.getUsersCard(cardId);
        log.debug("CardController: Данные карты id={} получены", cardId);

        return ResponseEntity.ok(card);
    }

    /**
     * Отправляет запрос на блокировку карты текущего пользователя по идентификатору
     * @param cardId - идентификатор карты
     * @return {@link CardBlockRequest} запрос на блокировку
     */
    @PostMapping("/{cardId}/block-request")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<CardBlockRequest> makeCardBlockRequest(@PathVariable Integer cardId) {
        log.info("CardController: Пользователь отправил запрос на блокировку карты id={}", cardId);

        CardBlockRequest request = cardBlockRequestService.makeRequest(cardId);
        log.debug("CardController: Запрос на блокировку карты id={} создан: {}", cardId, request);

        return ResponseEntity.ok(request);
    }

    /**
     * Выполняет перевод между картами пользователя, выполняющего запрос,
     * на основе данных формы
     * @param transferRequest - форма перевода (откуда, куда и сумма)
     * @return {@link Map} с сообщением о результате транзакции
     */
    @PostMapping("/transfer")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Map<String, String>> transfer(@RequestBody @Valid TransferRequest transferRequest) {
        log.info("CardController: Пользователь инициировал перевод между своими картами. От cardId={}, к cardId={}",
                transferRequest.getFromCardId(), transferRequest.getToCardId());

        Map<String, String> result = transferService.transferBetweenOwnCards(transferRequest);
        log.debug("CardController: Результат перевода: {}", result);

        return ResponseEntity.ok(result);
    }
}
