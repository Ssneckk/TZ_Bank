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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
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
    private CardController cardController;
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

        cardController = new CardController(cardService, transferService, cardBlockRequestService);

        mockMvc = MockMvcBuilders.standaloneSetup(cardController).build();
    }

    @Test
    void getUsersCards(){

        Page<SimpleCardRecordDTO> simpleCardRecordDTOPage =
                new PageImpl<>(singletonList(simpleCardRecordDTO));

        when(cardService.getUsersCards(any(Pageable.class))).thenReturn(simpleCardRecordDTOPage);

        Page<SimpleCardRecordDTO> result =
                cardController.getUsersCards(PageRequest.of(0, 10)).getBody();

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo(simpleCardRecordDTOPage.getContent());
        assertThat(result.getTotalElements()).isEqualTo(1);
    }
    @Test
    void getCard_shouldReturnFullCard() throws Exception {
        FullCardRecordDTO fullCard = fullCardRecordDTO;
        when(cardService.getUsersCard(1)).thenReturn(fullCard);

        mockMvc.perform(get("/api/cards/1"))
                .andExpect(status().isOk());

        verify(cardService).getUsersCard(1);
    }

    @Test
    void makeCardBlockRequest_shouldReturnCardBlockRequest() throws Exception {
        CardBlockRequest blockRequest = new CardBlockRequest();
        when(cardBlockRequestService.makeRequest(1)).thenReturn(blockRequest);

        mockMvc.perform(post("/api/cards/1/block-request"))
                .andExpect(status().isOk());

        verify(cardBlockRequestService).makeRequest(1);
    }

    @Test
    void transfer_shouldReturnMapMessage() throws Exception {
        TransferRequest transferRequest = new TransferRequest(1, 2, new BigDecimal("100.00"));
        Map<String, String> response = Map.of("message", "Транзакция успешно завершена");

        when(transferService.transferBetweenOwnCards(any(TransferRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/cards/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Транзакция успешно завершена"));

        verify(transferService).transferBetweenOwnCards(any(TransferRequest.class));
    }
}
