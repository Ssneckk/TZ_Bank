package com.example.bankcards.service.transfer.impls;

import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.TransferException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.security.jwt.JwtProvider;
import com.example.bankcards.service.transfer.TransferService;
import com.example.bankcards.util.auxiliaryclasses.request.TransferRequest;
import com.example.bankcards.util.enums.CardStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransferServiceImplTest {

    private JwtProvider jwtProvider;
    private CardRepository cardRepository;
    private TransferService transferService;

    @BeforeEach
    void setUp() {
        jwtProvider = Mockito.mock(JwtProvider.class);
        cardRepository = Mockito.mock(CardRepository.class);
        transferService = new TransferServiceImpl(jwtProvider, cardRepository);
    }

    @Test
    void transferBetweenOwnCards_shouldReturnSuccess() {
        String token = "token";
        int userId = 2;
        int cardFromId = 1;
        int cardToId = 3;
        BigDecimal amount = new BigDecimal("100.00");
        TransferRequest transferRequest = new TransferRequest(cardFromId,cardToId, amount);

        Card cardFrom = new Card();
        cardFrom.setStatus(CardStatusEnum.ACTIVE);
        cardFrom.setBalance(BigDecimal.valueOf(1000.00));

        Card cardTo = new Card();
        cardTo.setStatus(CardStatusEnum.ACTIVE);
        cardTo.setBalance(BigDecimal.valueOf(2000.00));

        when(jwtProvider.extrackId(token)).thenReturn(userId);

        when(cardRepository.existsByIdAndUserId(cardFromId, userId)).thenReturn(true);
        when(cardRepository.existsByIdAndUserId(cardToId, userId)).thenReturn(true);

        when(cardRepository.findById(cardFromId)).thenReturn(Optional.of(cardFrom));
        when(cardRepository.findById(cardToId)).thenReturn(Optional.of(cardTo));

        String successMessage = "Транзакция успешно завершена";

        Map<String, String> message = transferService.transferBetweenOwnCards(transferRequest, token);

        assertEquals(successMessage, message.get("message"));
        verify(jwtProvider).extrackId(token);
        verify(cardRepository).existsByIdAndUserId(cardFromId, userId);
        verify(cardRepository).existsByIdAndUserId(cardToId, userId);
        verify(cardRepository).findById(cardFromId);
        verify(cardRepository).findById(cardToId);
    }

    @Test
    void transferBetweenOwnCards_shouldCheckOwnerException() {
        String token = "token";
        int userId = 2;
        int cardFromId = 1;
        int cardToId = 3;
        BigDecimal amount = new BigDecimal("100.00");
        TransferRequest transferRequest = new TransferRequest(cardFromId,cardToId, amount);

        when(jwtProvider.extrackId(token)).thenReturn(userId);

        when(cardRepository.existsByIdAndUserId(cardFromId, userId)).thenReturn(true);
        when(cardRepository.existsByIdAndUserId(cardToId, userId)).thenReturn(false);

        assertThrows(TransferException.class , () -> transferService.transferBetweenOwnCards(transferRequest, token));
        verify(jwtProvider).extrackId(token);
        verify(cardRepository).existsByIdAndUserId(cardFromId, userId);
        verify(cardRepository).existsByIdAndUserId(cardToId, userId);
    }
}