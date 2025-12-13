package com.example.bankcards.service.card.impls;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.exceptions.CardBlockRequestException;
import com.example.bankcards.repository.CardBlockRequestRepository;
import com.example.bankcards.service.card.CardBlockRequestService;
import com.example.bankcards.service.card.CardService;
import com.example.bankcards.service.user.UserService;
import com.example.bankcards.util.enums.CardStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CardBlockRequestServiceImpl implements CardBlockRequestService {

    private final CardBlockRequestRepository cardBlockRequestRepository;
    private final CardService cardService;
    private final UserService userService;

    @Autowired
    public CardBlockRequestServiceImpl(CardBlockRequestRepository cardBlockRequestRepository, CardService cardService, UserService userService) {
        this.cardBlockRequestRepository = cardBlockRequestRepository;
        this.cardService = cardService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public CardBlockRequest makeRequest(Integer cardId) {
        User user = userService.findCurrentUser();
        Card card = cardService.findCardById(cardId);
        List<Card> usersCards = user.getCards();

        if (!usersCards.contains(card)) {
            throw new CardBlockRequestException("Вы не можете подать заявку на блокировку не своей карты");
        }
        Optional<CardBlockRequest> exists = cardBlockRequestRepository.findByCardId(cardId);

        if (exists.isPresent()) {
            throw new CardBlockRequestException("Уже есть активная заявка на блокировку этой карты");
        }

        if(card.getStatus().equals(CardStatusEnum.BLOCKED) || card.getStatus() == CardStatusEnum.DELETED) {
            throw new CardBlockRequestException("Карта заблокирована либо удалена");
        }

        CardBlockRequest cardBlockRequest =
                new CardBlockRequest(cardId, user.getId(), LocalDate.now());

        return cardBlockRequestRepository.save(cardBlockRequest);
    }

    @Override
    @Transactional
    public Map<String, String> blockByRequest(Integer cardBlockRequestId) {

        CardBlockRequest cardBlockRequest = findCardBlockRequest(cardBlockRequestId);

        Map<String, String> response = new HashMap<>();

        Integer cardId = cardBlockRequest.getCardId();
        Card card = cardService.findCardById(cardId);

        card.setStatus(CardStatusEnum.BLOCKED);

        cardBlockRequestRepository.delete(cardBlockRequest);

        response.put("Заблокирована","карта с id: " + cardId + " заблокирована");

        return response;
    }

    @Override
    public Page<CardBlockRequest> getRequests(Pageable pageable) {
        return cardBlockRequestRepository.findAll(pageable);
    }


    private CardBlockRequest findCardBlockRequest(Integer cardBlockRequestId) {
        return cardBlockRequestRepository.findById(cardBlockRequestId)
                .orElseThrow(() -> new CardBlockRequestException(" Заявка на блокировку карты с id: "
                                + cardBlockRequestId + " не найдена"));
    }
}
