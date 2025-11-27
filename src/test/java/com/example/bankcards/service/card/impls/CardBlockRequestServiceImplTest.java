package com.example.bankcards.service.card.impls;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardBlockRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.jwt.JwtProvider;
import com.example.bankcards.service.card.CardBlockRequestService;
import com.example.bankcards.service.card.CardService;
import com.example.bankcards.util.enums.CardStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class CardBlockRequestServiceImplTest {

    private CardBlockRequestRepository cardBlockRequestRepository;
    private UserRepository userRepository;
    private CardService cardService;
    private JwtProvider jwtProvider;
    private CardRepository cardRepository;

    private CardBlockRequestService cardBlockRequestService;

    @BeforeEach
    void setUp() {
        cardBlockRequestRepository = Mockito.mock(CardBlockRequestRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        cardService = Mockito.mock(CardService.class);
        jwtProvider = Mockito.mock(JwtProvider.class);
        cardRepository = Mockito.mock(CardRepository.class);

        cardBlockRequestService = new CardBlockRequestServiceImpl(
                cardBlockRequestRepository,
                userRepository,
                cardService,
                jwtProvider);
    }

    @Test
    void makeRequest_shouldCallDependencies() {
        String authHeader = "authHeader";
        int cardId = 2;
        int userId = 1;

        User user = new User();
        Card card = new Card();
        user.setCards(new ArrayList<>());
        user.getCards().add(card);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(jwtProvider.extrackId(authHeader)).thenReturn(userId);
        when(cardService.findCardById(cardId)).thenReturn(card);

        when(cardBlockRequestRepository.findByCardId(cardId)).thenReturn(Optional.empty());

        cardBlockRequestService.makeRequest(cardId, authHeader);

        verify(jwtProvider).extrackId(authHeader);
        verify(userRepository).findById(userId);
        verify(cardService).findCardById(cardId);
        verify(cardBlockRequestRepository).findByCardId(cardId);
    }

    @Test
    void blockByRequest_shouldBlockCardAndReturnMessage() {

        CardBlockRequest cardBlockRequest = new CardBlockRequest();
        cardBlockRequest.setCardId(100);
        when(cardBlockRequestRepository.findById(anyInt())).thenReturn(Optional.of(cardBlockRequest));

        Card card = new Card();
        when(cardService.findCardById(100)).thenReturn(card);

        Map<String, String> response = cardBlockRequestService.blockByRequest(1);

        assertEquals(CardStatusEnum.BLOCKED, card.getStatus());
        assertTrue(response.containsKey("Заблокирована"));
        assertEquals("карта с id: 100 заблокирована", response.get("Заблокирована"));

        verify(cardService).findCardById(100);
        verify(cardBlockRequestRepository).delete(any(CardBlockRequest.class));
    }

    @Test
    void getRequests_shouldCallDependencies() {
        CardBlockRequest cardBlockRequest = new CardBlockRequest();
        Page<CardBlockRequest> cardBlockRequestPage = new PageImpl<>(List.of(cardBlockRequest));

        PageRequest pageRequest = PageRequest.of(0, 10);

        when(cardBlockRequestRepository.findAll(pageRequest)).thenReturn(cardBlockRequestPage);

        Page<CardBlockRequest> result = cardBlockRequestService.getRequests(pageRequest);

        assertEquals(1, result.getContent().size());
        assertEquals(cardBlockRequest, result.getContent().get(0));

        verify(cardBlockRequestRepository).findAll(pageRequest);
    }
}