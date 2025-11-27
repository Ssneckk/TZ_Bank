package com.example.bankcards.service.card.impls;

import com.example.bankcards.entity.Card;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.card.CardService;
import com.example.bankcards.util.enums.CardStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardStatusServiceImplTest {

    private CardRepository cardRepository;
    private CardService cardService;
    private CardStatusServiceImpl cardStatusService;

    @BeforeEach
    void setUp() {
        cardRepository = Mockito.mock(CardRepository.class);
        cardService = Mockito.mock(CardService.class);
        cardStatusService = new CardStatusServiceImpl(cardRepository, cardService);
    }

    @Test
    void setCardsExpired_shouldCallRepository() {
        when(cardRepository.markExpired(LocalDate.now())).thenReturn(5);

        cardStatusService.setCardsExpired();

        verify(cardRepository).markExpired(LocalDate.now());
    }

    @Test
    void blockCard_shouldBlockCardAndReturnMessage() {
        Card card = new Card();
        when(cardService.findCardById(1)).thenReturn(card);

        Map<String, String> result = cardStatusService.blockCard(1);

        assertEquals(CardStatusEnum.BLOCKED, card.getStatus());
        assertEquals("Карта с id: 1 заблокирована", result.get("message of block: "));
        verify(cardService).findCardById(1);
    }

    @Test
    void activateCard_shouldActivateCardAndReturnMessage() {
        Card card = new Card();
        when(cardService.findCardById(2)).thenReturn(card);

        Map<String, String> result = cardStatusService.activateCard(2);

        assertEquals(CardStatusEnum.ACTIVE, card.getStatus());
        assertEquals("Карта с id: 2 активирована", result.get("message of activate: "));
        verify(cardService).findCardById(2);
    }
}
