package com.example.bankcards.service.card.impls;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardBlockRequestException;
import com.example.bankcards.exception.UserException;
import com.example.bankcards.repository.CardBlockRequestRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.jwt.JwtProvider;
import com.example.bankcards.service.card.CardBlockRequestService;
import com.example.bankcards.service.card.CardService;
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
    private final UserRepository userRepository;
    private final CardService cardService;
    private final JwtProvider jwtProvider;

    @Autowired
    public CardBlockRequestServiceImpl(CardBlockRequestRepository cardBlockRequestRepository, UserRepository userRepository, CardService cardService, JwtProvider jwtProvider) {
        this.cardBlockRequestRepository = cardBlockRequestRepository;
        this.userRepository = userRepository;
        this.cardService = cardService;
        this.jwtProvider = jwtProvider;
    }

    @Override
    @Transactional
    public CardBlockRequest makeRequest(Integer cardId, String authHeader) {
        int userId = jwtProvider.extrackId(authHeader);
        User user = findUser(userId);
        Card card = cardService.findCardById(cardId);
        List<Card> usersCards = user.getCards();

        if (!usersCards.contains(card)) {
            throw new CardBlockRequestException("Вы не можете подать заявку на блокировку не своей карты");
        }
        Optional<CardBlockRequest> exists = cardBlockRequestRepository.findByCardId(cardId);

        if (exists.isPresent()) {
            throw new CardBlockRequestException("Уже есть активная заявка на блокировку этой карты");
        }

        //Можно вынести это в отдельный метод используя Factory pattern,
        // но я решил сделать так
        CardBlockRequest cardBlockRequest = new CardBlockRequest();
        cardBlockRequest.setCardId(cardId);
        cardBlockRequest.setRequestedUserId(userId);
        cardBlockRequest.setRequestedAt(LocalDate.now());

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


    private User findUser(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(()->new UserException("Пользователь с id: "
                        + userId + " не найден"));
    }

    private CardBlockRequest findCardBlockRequest(Integer cardBlockRequestId) {
        return cardBlockRequestRepository.findById(cardBlockRequestId)
                .orElseThrow(() -> new CardBlockRequestException(" Заявка на блокировку карты с id: "
                                + cardBlockRequestId + " не найдена"));
    }
}
