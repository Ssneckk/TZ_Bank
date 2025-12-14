package com.example.bankcards.service.transfer.impls;

import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.exceptions.CardException;
import com.example.bankcards.exception.exceptions.TransferException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.card.CardService;
import com.example.bankcards.service.transfer.TransferService;
import com.example.bankcards.service.user.UserService;
import com.example.bankcards.util.auxiliaryclasses.request.TransferRequest;
import com.example.bankcards.util.enums.CardStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Сервисный слой, реализующий {@link TransferService}, отвечающий за
 * переводы.
 * <p>Предоставляет метод:</p>
 * <ul>
 *     <li>Перевод между своими картами</li>
 * </ul>
 */
@Service
public class TransferServiceImpl implements TransferService {

    private static final Logger log = LoggerFactory.getLogger(TransferServiceImpl.class);

    private final CardRepository cardRepository;
    private final UserService userService;
    private final CardService cardService;

    @Autowired
    public TransferServiceImpl(CardRepository cardRepository, UserService userService, CardService cardService) {
        this.cardRepository = cardRepository;
        this.userService = userService;
        this.cardService = cardService;
    }

    /**
     * Переводит сумму с одной карты пользователя на другую карту.
     * <p>Транзакция не пройдет если:</p>
     * <ul>
     *     <li>Одна из карт не найдена;</li>
     *     <li>На карте отправителе недостаточно средств;</li>
     *     <li>Одна или обе карты не принадлежат пользователю;</li>
     *     <li>Одна или обе карты заблокированы.</li>
     * </ul>
     * @param transferRequest объект {@link TransferRequest} с данными перевода,
     *                        идентификатор карты отправителя, идентификатор карты получателя,
     *                        сумма перевода
     * @return {@link Map} Сообщение о результате транзакции
     * @throws TransferException - если одна или обе карты не принадлежат текущему пользователю,
     * одна или обе карты не существуют, на карте отправителе недостаточно средств,
     * одна либо обе карты заблокированы
     */
    @Override
    @Transactional
    public Map<String, String> transferBetweenOwnCards(TransferRequest transferRequest) {
        int userId = userService.getCurrentUserId();
        int cardFromId = transferRequest.getFromCardId();
        int cardToId = transferRequest.getToCardId();
        BigDecimal amount = transferRequest.getAmount();

        log.info("Начало перевода {} с карты {} на карту {} для пользователя {}",
                amount, cardFromId, cardToId, userId);

        //Проверка карт на принадлежность текущему пользователю
        checkIsUserOwnerBoth(cardFromId, cardToId, userId);

        Card cardFrom = cardService.findCardById(cardFromId);

        //Проверка баланса на карте
        checkBalanceFromCard(cardFrom.getBalance(), amount);

        Card cardTo = cardService.findCardById(cardToId);

        //Проверка статусов карт
        checkCardsStatus(cardFrom, cardTo);

        cardFrom.setBalance(cardFrom.getBalance().subtract(amount));
        cardTo.setBalance(cardTo.getBalance().add(amount));

        log.info("Перевод {} успешно выполнен с карты {} на карту {} для пользователя {}. Балансы: от {} до {}",
                amount, cardFromId, cardToId, userId, cardFrom.getBalance(), cardTo.getBalance());

        Map<String,String> response = new HashMap<>();
        response.put("message", "Транзакция успешно завершена");

        return response;
    }

    protected void checkBalanceFromCard(BigDecimal cardsFromBalance, BigDecimal amountToTransfer) {
        if(cardsFromBalance.compareTo(amountToTransfer)<0){
            log.warn("Недостаточно средств на карте. Требуется: {}, доступно: {}", amountToTransfer, cardsFromBalance);
            throw new TransferException("на карте недостаточно средств");
        }
    }

    protected void checkIsUserOwnerBoth(int card1, int card2, int userId) {
        boolean card1Exists = cardRepository.existsByIdAndUserId(card1, userId);
        boolean card2Exists = cardRepository.existsByIdAndUserId(card2, userId);

        if(!card1Exists || !card2Exists) {
            log.warn("Одна или обе карты не принадлежат пользователю {}. Карты: {}, {}", userId, card1, card2);
            throw new TransferException("Одна из карт вам не принадлежит");
        }
    }

    protected void checkCardsStatus(Card fromCardId, Card toCardId) {

        CardStatusEnum fromCardStatusEnum = fromCardId.getStatus();
        CardStatusEnum toCardStatusEnum = toCardId.getStatus();

        if (!fromCardStatusEnum.equals(CardStatusEnum.ACTIVE)||!toCardStatusEnum.equals(CardStatusEnum.ACTIVE)) {
            log.warn("Одна либо обе карты заблокированы. Карты: {} - {}, {} - {}",
                    fromCardId, fromCardStatusEnum, toCardId, toCardStatusEnum);
            throw new TransferException("Одна либо обе карты заблокированы");
        }
    }
}
