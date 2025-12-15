package com.example.bankcards.service.card.impls;

import com.example.bankcards.controller.AdminCardController;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.exceptions.CardBlockRequestException;
import com.example.bankcards.repository.CardBlockRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.card.CardBlockRequestService;
import com.example.bankcards.service.card.CardService;
import com.example.bankcards.service.user.UserService;
import com.example.bankcards.util.enums.CardStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Сервисный слой, который реализует {@link CardBlockRequestService},
 * отвечает за запросы на блокировку карт.
 * <p>
 *    Предостваляет методы:
 * </p>
 * <ul>
 *     <li>Создания запроса на блокировку карты;</li>
 *     <li>Блокировку карты по запросу;</li>
 *     <li>Получение всех запросов пользователей</li>
 * </ul>
 */
@Service
public class CardBlockRequestServiceImpl implements CardBlockRequestService {

    private final CardBlockRequestRepository cardBlockRequestRepository;
    private final CardService cardService;
    private final UserService userService;
    private final CardRepository cardRepository;
    private static final Logger log = LoggerFactory.getLogger(CardBlockRequestServiceImpl.class);

    @Autowired
    public CardBlockRequestServiceImpl(CardBlockRequestRepository cardBlockRequestRepository, CardService cardService, UserService userService, CardRepository cardRepository) {
        this.cardBlockRequestRepository = cardBlockRequestRepository;
        this.cardService = cardService;
        this.userService = userService;
        this.cardRepository = cardRepository;
    }

    /**
     * Создает запрос на блокировку карты текущего пользователя,
     * по идентификатору карты
     * <p>Запрос может быть создан только при соблюдении следующих условий:</p>
     * <ul>
     *     <li>карта принадлежит текущему пользователю;</li>
     *     <li>для карты отсутствует активный запрос на блокировку;</li>
     *     <li>карта не находится в статусе {@link CardStatusEnum#BLOCKED}
     *         или {@link CardStatusEnum#DELETED}.</li>
     * </ul>
     * @param cardId - идентификатор карты
     * @return {@link CardBlockRequest} возвращает обратно созданный запроса
     * @throws CardBlockRequestException если карта не принадлежит пользователю,
     * уже существует активная заявка, если заявка не найдена или карта заблокирована/удалена
     */
    @Override
    @Transactional
    public CardBlockRequest makeRequest(Integer cardId) {
        User currentUser = userService.findCurrentUser();
        Card card = cardService.findCardById(cardId);
        boolean exists = cardRepository.existsByIdAndUserId(cardId, currentUser.getId());

        log.info("CardBlockRequestService: Пользователь {} пытается создать заявку на блокировку карты с id {}", currentUser.getId(), cardId);

        //Проверка принадлежности пользователю
        if (!exists) {
            log.warn("CardBlockRequestService: Пользователь {} попытался заблокировать чужую карту {}", currentUser.getId(), cardId);
            throw new CardBlockRequestException("Вы не можете подать заявку на блокировку не своей карты");
        }

        Optional<CardBlockRequest> optionalCardBlockRequest = cardBlockRequestRepository.findByCardId(cardId);

        //Проверка есть ли уже активная заявка на блокировку этой карты
        if (optionalCardBlockRequest.isPresent()) {
            log.warn("CardBlockRequestService: Пользователь {} попытался создать дубликат заявки на карту {}", currentUser.getId(), cardId);
            throw new CardBlockRequestException("Уже есть активная заявка на блокировку этой карты");
        }

        //Проверка текущего статуса карты
        if(card.getStatus().equals(CardStatusEnum.BLOCKED) || card.getStatus() == CardStatusEnum.DELETED) {
            log.warn("CardBlockRequestService: Пользователь {} попытался заблокировать карту {} со статусом {}", currentUser.getId(), cardId, card.getStatus());
            throw new CardBlockRequestException("Карта заблокирована либо удалена");
        }

        CardBlockRequest cardBlockRequest =
                new CardBlockRequest(cardId, currentUser.getId(), LocalDate.now());
        CardBlockRequest savedRequest = cardBlockRequestRepository.save(cardBlockRequest);

        log.info("CardBlockRequestService: Создана заявка на блокировку карты {} пользователем {}", cardId, currentUser.getId());

        return savedRequest;
    }

    /**
     * Блокирует карту по идентификатору заявки на блокировку карты
     * @param cardBlockRequestId - идентификатор заявки на блокировку карты
     * @return {@link Map} сообщение о результате операции
     * @throws CardBlockRequestException если заявка не найдена
     */
    @Override
    @Transactional
    public Map<String, String> blockByRequest(Integer cardBlockRequestId) {
        log.info("Начало блокировки карты по заявке {}", cardBlockRequestId);

        CardBlockRequest cardBlockRequest = findCardBlockRequest(cardBlockRequestId);

        Map<String, String> response = new HashMap<>();

        Integer cardId = cardBlockRequest.getCardId();
        Card card = cardService.findCardById(cardId);

        card.setStatus(CardStatusEnum.BLOCKED);

        cardBlockRequestRepository.delete(cardBlockRequest);

        response.put("Заблокирована","карта с id: " + cardId + " заблокирована");

        log.info("Карта {} успешно заблокирована по заявке {}", cardId, cardBlockRequestId);
        return response;
    }

    /**
     * Возвращает страницу с запросами на блокировку карт пользователей,
     * с возможностью пагинации и сортировки по параметрам
     * @param pageable - параметры пагинации и сортировки
     * @return {@link Page} страница с {@link CardBlockRequest}
     */
    @Override
    public Page<CardBlockRequest> getRequests(Pageable pageable) {
        log.info("Получение списка заявок на блокировку карт с пагинацией {}", pageable);
        return cardBlockRequestRepository.findAll(pageable);
    }


    /**
     * Возвращает запрос на блокировку карты по его идентификатору.
     * @param cardBlockRequestId идентификатор запроса на блокировку карты
     * @return {@link CardBlockRequest} найденный запрос
     * @throws CardBlockRequestException если запрос с указанным идентификатором не найден
     */
    private CardBlockRequest findCardBlockRequest(Integer cardBlockRequestId) {
        return cardBlockRequestRepository.findById(cardBlockRequestId)
                .orElseThrow(() ->{
                    log.error("Заявка на блокировку карты с id {} не найдена", cardBlockRequestId);
                    return new CardBlockRequestException(" Заявка на блокировку карты с id: "
                            + cardBlockRequestId + " не найдена");
                });
    }
}
