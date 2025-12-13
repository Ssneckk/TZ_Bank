package com.example.bankcards.service.card.impls;

import com.example.bankcards.dto.FullCardRecordDTO;
import com.example.bankcards.dto.SimpleCardRecordDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.security.jwt.JwtProvider;
import com.example.bankcards.service.card.CardService;
import com.example.bankcards.service.user.UserService;
import com.example.bankcards.util.converters.CardConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CardServiceImplTest {

    private CardRepository cardRepository;
    private CardConverter cardConverter;
    private UserService userService;

    private CardService cardService;

    @BeforeEach
    void setUp() {
        cardRepository = Mockito.mock(CardRepository.class);
        cardConverter = Mockito.mock(CardConverter.class);
        userService = Mockito.mock(UserService.class);
        cardService = new CardServiceImpl(cardRepository, cardConverter,userService);
    }

    @Test
    void getCards_shouldReturnMappedPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Card card = new Card();
        Page<Card> cardPage = new PageImpl<>(List.of(card));

        when(cardRepository.findAll(pageable)).thenReturn(cardPage);
        when(cardConverter.convertToSimpleCardRecord(card)).thenReturn(new SimpleCardRecordDTO(1, "test", "test"));

        Page<SimpleCardRecordDTO> result = cardService.getCards(pageable);

        assertEquals(1, result.getContent().size());
        verify(cardRepository).findAll(pageable);
        verify(cardConverter).convertToSimpleCardRecord(card);
    }

    @Test
    void getUsersCards_shouldReturnMappedPage() {
        int userId = 1;
        Pageable pageable = PageRequest.of(0, 10);
        Card card = new Card();
        Page<Card> cardPage = new PageImpl<>(List.of(card));

        when(userService.getCurrentUserId()).thenReturn(userId);
        when(cardRepository.findByUserId(userId, pageable)).thenReturn(cardPage);
        when(cardConverter.convertToSimpleCardRecord(card)).thenReturn(new SimpleCardRecordDTO(1, "test", "test"));

        Page<SimpleCardRecordDTO> result = cardService.getUsersCards(pageable);

        assertEquals(1, result.getContent().size());
        verify(userService).getCurrentUserId();
        verify(cardRepository).findByUserId(userId, pageable);
        verify(cardConverter).convertToSimpleCardRecord(card);
    }

    @Test
    void getCard_shouldReturnFullCardRecordDTO() {
        String authHeader = "auth";
        int cardId = 1;
        int userId = 2;
        Card card = new Card();
        FullCardRecordDTO dto = new FullCardRecordDTO(1,"test","test","test", BigDecimal.ZERO);

        when(userService.getCurrentUserId()).thenReturn(userId);
        when(cardRepository.existsByIdAndUserId(cardId, userId)).thenReturn(true);
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(cardConverter.convertToFullCardRecord(card)).thenReturn(dto);

        FullCardRecordDTO result = cardService.getCard(cardId);

        assertEquals(dto, result);
        verify(userService).getCurrentUserId();
        verify(cardRepository).existsByIdAndUserId(cardId, userId);
        verify(cardConverter).convertToFullCardRecord(card);
    }



}