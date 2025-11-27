package com.example.bankcards.service.card.impls;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.exception.CardException;
import com.example.bankcards.repository.CardBlockRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.jwt.JwtProvider;
import com.example.bankcards.service.card.CardService;
import com.example.bankcards.util.enums.CardStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardDeletionServiceimplTest {

    private CardRepository cardRepository;
    private CardDeletionServiceimpl cardDeletionService;

    @BeforeEach
    void setUp() {
        cardRepository = Mockito.mock(CardRepository.class);
        cardDeletionService = new CardDeletionServiceimpl(cardRepository);
    }

    @Test
    void delete_shouldSetStatusToDeleted_whenBalanceIsZero() {
        Card card = new Card();
        card.setBalance(BigDecimal.ZERO);
        card.setStatus(CardStatusEnum.ACTIVE);

        when(cardRepository.findById(1)).thenReturn(Optional.of(card));

        cardDeletionService.delete(1);

        assertEquals(CardStatusEnum.DELETED, card.getStatus());
        verify(cardRepository).findById(1);
    }

    @Test
    void delete_shouldThrowException_whenBalanceIsNotZero() {
        Card card = new Card();
        card.setBalance(BigDecimal.valueOf(100));

        when(cardRepository.findById(1)).thenReturn(Optional.of(card));

        CardException exception = assertThrows(CardException.class, () -> cardDeletionService.delete(1));
        assertEquals("Баланс на карте не равен 0", exception.getMessage());
    }

    @Test
    void delete_shouldThrowException_whenCardNotFound() {
        when(cardRepository.findById(1)).thenReturn(Optional.empty());

        CardException exception = assertThrows(CardException.class, () -> cardDeletionService.delete(1));
        assertEquals("Карта с id: 1 не найдена", exception.getMessage());
    }
}
