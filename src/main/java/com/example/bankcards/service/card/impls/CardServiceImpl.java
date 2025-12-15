package com.example.bankcards.service.card.impls;

import com.example.bankcards.dto.FullCardRecordDTO;
import com.example.bankcards.dto.SimpleCardRecordDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.exceptions.CardException;
import com.example.bankcards.exception.exceptions.UserException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.card.CardService;
import com.example.bankcards.service.user.UserService;
import com.example.bankcards.util.converters.CardConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/** Сервисный слой, реализующий {@link CardService}.
 * Отвечает за обработку CRUD-операций с банковскими картами.
 * <p>Предоставляет методы:</p>
 * <ul>
 *    <li>Получение всех карт пользователей с пагинацией;</li>
 *    <li>Получение карт текущего пользователя с пагинацией;</li>
 *    <li>Получение полной информации о карте текущего пользователя по идентификатору;</li>
 *    <li>Поиск карты по идентификатору без проверки владельца.</li>
 * </ul>
 */
@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardConverter cardConverter;
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(CardServiceImpl.class);

    @Autowired
    public CardServiceImpl(CardRepository cardRepository, CardConverter cardConverter,UserService userService) {
        this.cardRepository = cardRepository;
        this.cardConverter = cardConverter;
        this.userService = userService;
    }

    @Override
    public Page<SimpleCardRecordDTO> getCards(Pageable pageable) {
        log.info("CardService: Получение всех карт пользователей с параметрами pageable: {}", pageable);

        Page<SimpleCardRecordDTO> simpleCardRecordDTOPage = cardRepository.findAll(pageable)
                .map(cardConverter::convertToSimpleCardRecord);
        log.debug("CardService: Найдено {} карт", simpleCardRecordDTOPage.getTotalElements());

        return simpleCardRecordDTOPage;
    }

    @Override
    public Page<SimpleCardRecordDTO> getUsersCards(Pageable pageable) {
        int userId = userService.getCurrentUserId();
        log.info("CardService: Получение карт пользователя с id {} с параметрами pageable: {}", userId, pageable);

        Page<SimpleCardRecordDTO> simpleCardRecordDTOPage = cardRepository.findByUserId(userId, pageable)
                .map(cardConverter::convertToSimpleCardRecord);
        log.debug("CardService: Найдено {} карт у пользователя {}", simpleCardRecordDTOPage.getTotalElements(), userId);

        return simpleCardRecordDTOPage;
    }

    @Override
    public FullCardRecordDTO getUsersCard(Integer cardId) {
        int userId = userService.getCurrentUserId();
        log.info("CardService: Получение полной информации о карте {} для пользователя {}", cardId, userId);

        Card usersCard = findCardById(cardId);

        boolean exists = cardRepository.existsByIdAndUserId(cardId, userId);

        if(!exists) {
            log.warn("CardService: Карта {} не принадлежит пользователю {}", cardId, userId);
            throw new UserException("Карта не найдена, проверьте свои карты");
        }

        log.debug("CardService: Карта {} найдена для пользователя {}", cardId, userId);
        return cardConverter.convertToFullCardRecord(usersCard);
    }

    @Override
    public Card findCardById(Integer cardId) {
        log.info("CardService: Поиск карты по id {}", cardId);
        return cardRepository.findById(cardId)
                .orElseThrow(() -> {
                    log.warn("CardService: Карта с id {} не найдена", cardId);
                    return new CardException("Карта с id: " + cardId + " не найдена");
                });
    }
}
