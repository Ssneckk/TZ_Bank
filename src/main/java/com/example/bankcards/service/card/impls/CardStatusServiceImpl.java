package com.example.bankcards.service.card.impls;

import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.exceptions.CardException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.card.CardService;
import com.example.bankcards.service.card.CardStatusService;
import com.example.bankcards.util.enums.CardStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Сервисный слой, реализующий {@link CardStatusService}.
 * Отвечает за управление статусами карт:
 * активацию, блокировку и автоматическое истечение срока действия карт.
 * <p>
 * Методы класса предоставляют возможности:
 * </p>
 * <ul>
 *     <li>Автоматически помечать просроченные карты как истекшие;</li>
 *     <li>Блокировать карту по идентификатору;</li>
 *     <li>Активировать карту по идентификатору.</li>
 * </ul>
 */
@Service
public class CardStatusServiceImpl implements CardStatusService {

    private static final Logger log = LoggerFactory.getLogger(CardStatusServiceImpl.class);
    private final CardRepository cardRepository;
    private final CardService cardService;

    @Autowired
    public CardStatusServiceImpl(CardRepository cardRepository, CardService cardService) {
        this.cardRepository = cardRepository;
        this.cardService = cardService;
    }

    /**
     * Автоматически обновляет статус всех просроченных карт.
     * <p>
     * Метод помечает все карты с датой истечения до текущей как истекшие.
     * Запускается автоматически с фиксированной задержкой в 24 часа.
     * </p>
     * */
    @Override
    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    @Transactional
    public void setCardsExpired() {
        int e = cardRepository.markExpired(LocalDate.now());
        log.info("Автоматическая проверка и истечение срока карт выполнена. Всего просроченных карт: {}", e);
    }

    /**
     * Блокирует карту по ее идентификатору.
     *
     * @param cardId идентификатор карты для блокировки
     * @return {@link Map} с сообщением о выполненной блокировке
     * @throws CardException если карта уже заблокирована или удалена
     */
    @Override
    @Transactional
    public Map<String, String> blockCard(Integer cardId) {
        log.info("Попытка блокировки карты с id {}", cardId);

        Card card = cardService.findCardById(cardId);
        CardStatusEnum cardStatus = card.getStatus();

        if (cardStatus == CardStatusEnum.BLOCKED || cardStatus == CardStatusEnum.DELETED) {
            log.warn("Карта {} уже заблокирована или удалена. Блокировка невозможна.", cardId);
            throw new CardException("Карта уже заблокирована либо удалена");
        }

        card.setStatus(CardStatusEnum.BLOCKED);
        log.info("Карта {} успешно заблокирована", cardId);

        Map<String, String> response = new HashMap<>();
        response.put("message of block: ", "Карта с id: " + cardId +" заблокирована");
        return response;
    }

    /**
     * Активирует карту по ее идентификатору.
     *
     * @param cardId идентификатор карты для активации
     * @return {@link Map} с сообщением о выполненной активации
     * @throws CardException если карта удалена
     */
    @Override
    @Transactional
    public Map<String, String> activateCard(Integer cardId) {
        log.info("Попытка активации карты с id {}", cardId);

        Card card = cardService.findCardById(cardId);
        CardStatusEnum cardStatus = card.getStatus();

        if (cardStatus == CardStatusEnum.DELETED) {
            log.warn("Карта {} удалена. Активация невозможна.", cardId);
            throw new CardException("Карта удалена");
        }

        card.setStatus(CardStatusEnum.ACTIVE);
        log.info("Карта {} успешно активирована", cardId);

        Map<String, String> response = new HashMap<>();
        response.put("message of activate: ", "Карта с id: " + cardId +" активирована");
        return response;
    }
}
