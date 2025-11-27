package com.example.bankcards.controller;

import com.example.bankcards.dto.FullCardRecordDTO;
import com.example.bankcards.dto.SimpleCardRecordDTO;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.service.card.CardBlockRequestService;
import com.example.bankcards.service.card.CardService;
import com.example.bankcards.service.transfer.TransferService;
import com.example.bankcards.util.auxiliaryclasses.request.TransferRequest;
import com.example.bankcards.util.enums.CardStatusEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CardControllerTest {

    private MockMvc mockMvc;
    private CardService cardService;
    private TransferService transferService;
    private CardBlockRequestService cardBlockRequestService;
    private ObjectMapper objectMapper;
    private FullCardRecordDTO fullCardRecordDTO = new FullCardRecordDTO(1,
            "**** **** **** 8888", "12/24",
            CardStatusEnum.ACTIVE.toString(),BigDecimal.ZERO);
    private SimpleCardRecordDTO simpleCardRecordDTO = new SimpleCardRecordDTO(1,
            "**** **** **** 8888", CardStatusEnum.ACTIVE.toString());

    @BeforeEach
    void setUp() {
        cardService = Mockito.mock(CardService.class);
        transferService = Mockito.mock(TransferService.class);
        cardBlockRequestService = Mockito.mock(CardBlockRequestService.class);
        objectMapper = new ObjectMapper();

        CardController cardController = new CardController(cardService, transferService, cardBlockRequestService);

        mockMvc = MockMvcBuilders.standaloneSetup(cardController).build();
    }

 // У меня возникла проблема с Pageable, а времени разбираться с ним уже не осталось, надо сдавать ТЗ :(

    @Test
    void getCard_shouldReturnFullCard() throws Exception {
        FullCardRecordDTO fullCard = fullCardRecordDTO;
        when(cardService.getCard(1, "Bearer token")).thenReturn(fullCard);

        mockMvc.perform(get("/api/cards/1")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk());

        verify(cardService).getCard(1, "Bearer token");
    }

    @Test
    void makeCardBlockRequest_shouldReturnCardBlockRequest() throws Exception {
        CardBlockRequest blockRequest = new CardBlockRequest();
        when(cardBlockRequestService.makeRequest(1, "Bearer token")).thenReturn(blockRequest);

        mockMvc.perform(post("/api/cards/1/block-request")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk());

        verify(cardBlockRequestService).makeRequest(1, "Bearer token");
    }

    @Test
    void transfer_shouldReturnMapMessage() throws Exception {
        TransferRequest transferRequest = new TransferRequest(1, 2, new BigDecimal("100.00"));
        Map<String, String> response = Map.of("message", "Транзакция успешно завершена");

        when(transferService.transferBetweenOwnCards(any(TransferRequest.class), anyString())).thenReturn(response);

        mockMvc.perform(post("/api/cards/transfer")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Транзакция успешно завершена"));

        verify(transferService).transferBetweenOwnCards(any(TransferRequest.class), anyString());
    }
}
