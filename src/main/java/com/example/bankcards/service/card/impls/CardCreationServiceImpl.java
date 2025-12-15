package com.example.bankcards.service.card.impls;

import com.example.bankcards.dto.FullCardRecordDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.exceptions.CryptoUtilException;
import com.example.bankcards.exception.exceptions.UserException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.card.CardCreationService;
import com.example.bankcards.service.card.CardNumberGenerator;
import com.example.bankcards.util.auxiliaryclasses.crypto.CryptoUtil;
import com.example.bankcards.util.converters.CardConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;

/**
 * Сервисный слой, который реализует {@link CardCreationService} и {@link CardNumberGenerator},
 * отвечает за создание карты.
 * <p> Предоставляет метод: </p>
 * <ul>
 *     <li>Создание карты</li>
 * </ul>
 */
@Service
public class CardCreationServiceImpl implements CardCreationService, CardNumberGenerator {

    private static final Logger log = LoggerFactory.getLogger(CardCreationServiceImpl.class);
    private final CryptoUtil cryptoUtil;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardConverter cardConverter;

    public CardCreationServiceImpl(CryptoUtil cryptoUtil, CardRepository cardRepository, UserRepository userRepository, CardConverter cardConverter) {
        this.cryptoUtil = cryptoUtil;
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.cardConverter = cardConverter;
    }

    //Я увидел в интернете, что это надо делать через алгоритм Луна,
    //для задачи на работе я бы разобрался, пока что реализовал генерацию карты так.
    /**
     * Генерирует 16-ти значный номер карты
     * @return {@link String} номер карты
     */
    @Override
    public String generateCardNumber() {

        int prefix = 4231;

        StringBuilder cardNumber = new StringBuilder(prefix);
        SecureRandom random = new SecureRandom();

        while (cardNumber.length() < 16) {
            cardNumber.append(random.nextInt(10));
        }
        return cardNumber.toString();

    }

    /**
     * Создает карту пользователю по идентификатору пользователя
     * @param userId - идентификатор пользователя
     * @return {@link FullCardRecordDTO} DTO с полной информацией
     * о созданной карте
     * @throws UserException если пользователь по идентификатору не найден
     * @throws CryptoUtilException если возникал проблема при шифровании карты
     */
    @Override
    @Transactional
    public FullCardRecordDTO createCard(Integer userId){
        log.info("CardCreationService: Начало создания карты для пользователя с id {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(()-> {
                    log.warn("CardCreationService: Пользователь с id {} не найден", userId);
                    return new UserException("User с id из токена: "
                        + userId + " не найден");});

        String cardNumber = generateCardNumber();
        log.debug("CardCreationService: Сгенерирован номер карты для пользователя {}: {}",
                userId, cardNumber.substring(cardNumber.length() - 4) + "****");

        String last4 = cardNumber.substring(cardNumber.length() - 4);

        cardNumber = cryptoUtil.encrypt(cardNumber);

        LocalDate expiry_date = LocalDate.now().plusYears(1);

        Card newCard = new Card(expiry_date,user,cardNumber,last4);

        newCard.setBalance(BigDecimal.valueOf(50000.00));

        Card savedCard = cardRepository.save(newCard);
        log.info("CardCreationService: Карта успешно создана для пользователя {}. ID карты: {}, последние 4 цифры: {}",
                userId, savedCard.getId(), last4);

        return cardConverter.convertToFullCardRecord(savedCard);
    }
}
