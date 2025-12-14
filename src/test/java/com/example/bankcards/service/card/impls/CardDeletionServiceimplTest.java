package com.example.bankcards.service.card.impls;

import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.exceptions.CardException;
import com.example.bankcards.repository.CardRepository;
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

    private CardService cardService;
    private CardDeletionServiceimpl cardDeletionService;

    @BeforeEach
    void setUp() {
        cardService = Mockito.mock(CardService.class);
        cardDeletionService = new CardDeletionServiceimpl(cardService);
    }

    @Test
    void delete_shouldSetStatusToDeleted_whenBalanceIsZero() {
        Card card = new Card();
        card.setBalance(BigDecimal.ZERO);
        card.setStatus(CardStatusEnum.ACTIVE);

        when(cardService.findCardById(1)).thenReturn(card);

        cardDeletionService.delete(1);

        assertEquals(CardStatusEnum.DELETED, card.getStatus());
        verify(cardService).findCardById(1);
    }

    @Test
    void delete_shouldThrowException_whenBalanceIsNotZero() {
        Card card = new Card();
        card.setBalance(BigDecimal.valueOf(100));

        when(cardService.findCardById(1)).thenReturn(card);

        CardException exception = assertThrows(CardException.class, () -> cardDeletionService.delete(1));
        assertEquals("Баланс на карте не равен 0", exception.getMessage());
    }
}
