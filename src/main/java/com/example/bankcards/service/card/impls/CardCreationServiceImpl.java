package com.example.bankcards.service.card.impls;

import com.example.bankcards.dto.FullCardRecordDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.exceptions.UserException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.card.CardCreationService;
import com.example.bankcards.util.auxiliaryclasses.crypto.CryptoUtil;
import com.example.bankcards.util.converters.CardConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class CardCreationServiceImpl implements CardCreationService {

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
    public String generateCardNumber() {

        int prefix = 4231;

        StringBuilder cardNumber = new StringBuilder(prefix);

        while (cardNumber.length() < 16) {
            cardNumber.append((int)(Math.random() * 10));
        }
        return cardNumber.toString();

    }

    @Override
    @Transactional
    public FullCardRecordDTO createCard(Integer user_id){

        User user = userRepository.findById(user_id)
                .orElseThrow(()-> new UserException("User с id из токена: "
                        + user_id + " не найден"));

        String cardNumber = generateCardNumber();

        String last4 = cardNumber.substring(cardNumber.length() - 4);

        cardNumber = cryptoUtil.encrypt(cardNumber);

        LocalDate expiry_date = LocalDate.now().plusYears(1);

        Card newCard = new Card(expiry_date,user,cardNumber,last4);

        newCard.setBalance(BigDecimal.valueOf(50000.00));

        Card savedCard = cardRepository.save(newCard);

        return cardConverter.convertToFullCardRecord(savedCard);
    }
}
