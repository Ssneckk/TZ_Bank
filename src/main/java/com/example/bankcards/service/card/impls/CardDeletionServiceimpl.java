package com.example.bankcards.service.card.impls;

import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.exceptions.CardException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.card.CardDeletionService;
import com.example.bankcards.service.card.CardService;
import com.example.bankcards.util.enums.CardStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Сервисный слой, реализует {@link CardDeletionService},
 * отвечает за удаление карты
 * <p>Предоставляет метод:</p>
 * <ul>
 *    <li>Удаление карты</li>
 * </ul>
 */
@Service
public class CardDeletionServiceimpl implements CardDeletionService {

    private final CardService cardService;
    private static final Logger log = LoggerFactory.getLogger(CardDeletionServiceimpl.class);

    public CardDeletionServiceimpl(CardService cardService) {
        this.cardService = cardService;
    }

    /**
     * Помечает карту как удалённую.
     * <p>
     * Удаление возможно только в случае, если баланс карты равен {@code 0}.
     * Физического удаления записи из базы данных не происходит —
     * карта переводится в статус {@link CardStatusEnum#DELETED}.
     * </p>
     *
     * @param cardId идентификатор карты
     * @throws CardException если карта не найдена
     *                       или если баланс карты не равен нулю
     */
    @Override
    @Transactional
    public void delete(Integer cardId) {
        log.info("Попытка удаления карты с id {}", cardId);

        Card card = cardService.findCardById(cardId);

        if(card.getBalance().compareTo(BigDecimal.ZERO)!=0){
            log.warn("Невозможно удалить карту с id {}. Баланс не равен нулю: {}", cardId, card.getBalance());
            throw new CardException("Баланс на карте не равен 0");
        }

        card.setStatus(CardStatusEnum.DELETED);
        log.info("Карта с id {} успешно помечена как DELETED", cardId);
    }
}
