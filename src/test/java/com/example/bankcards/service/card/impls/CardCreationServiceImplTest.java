package com.example.bankcards.service.card.impls;

import com.example.bankcards.dto.FullCardRecordDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.TokenException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.auxiliaryclasses.crypto.CryptoUtil;
import com.example.bankcards.util.converters.CardConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardCreationServiceImplTest {

    private CardRepository cardRepository;
    private UserRepository userRepository;
    private CryptoUtil cryptoUtil;
    private CardConverter cardConverter;
    private CardCreationServiceImpl cardService;

    @BeforeEach
    void setUp() {
        cardRepository = mock(CardRepository.class);
        userRepository = mock(UserRepository.class);
        cryptoUtil = mock(CryptoUtil.class);
        cardConverter = mock(CardConverter.class);
        cardService = new CardCreationServiceImpl(cryptoUtil, cardRepository, userRepository, cardConverter);
    }

    @Test
    void createCard_shouldVerify() throws Exception {

        Integer userId = 1;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        CardCreationServiceImpl spyService = spy(cardService);
        doReturn("4231000000001234").when(spyService).generateCardNumber();

        when(cryptoUtil.encrypt("4231000000001234")).thenReturn("encryptedCard1234");

        Card savedCard = new Card(LocalDate.now().plusYears(1), user, "encryptedCard1234", "1234");
        when(cardRepository.save(any(Card.class))).thenReturn(savedCard);

        FullCardRecordDTO fullCardRecordDTO =
                new FullCardRecordDTO(1,"**** **** **** 1234", "12/25","ACTIVE", BigDecimal.ZERO);
        when(cardConverter.convertToFullCardRecord(savedCard)).thenReturn(fullCardRecordDTO);

        spyService.createCard(userId);

        verify(userRepository).findById(userId);
        verify(cryptoUtil).encrypt("4231000000001234");
        verify(cardRepository).save(any(Card.class));
        verify(cardConverter).convertToFullCardRecord(savedCard);
    }

    @Test
    void createCard_userNotFound_shouldThrowException() {
        Integer userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(TokenException.class, () -> cardService.createCard(userId));

        verify(userRepository).findById(userId);
        verifyNoInteractions(cardRepository, cryptoUtil, cardConverter);
    }
}