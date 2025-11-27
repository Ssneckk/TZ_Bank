package com.example.bankcards.controller;

import com.example.bankcards.dto.FullCardRecordDTO;
import com.example.bankcards.dto.SimpleCardRecordDTO;
import com.example.bankcards.service.card.*;
import com.example.bankcards.util.enums.CardStatusEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminCardControllerTest {

    private CardCreationService cardCreationService;
    private CardService cardService;
    private CardStatusService cardStatusService;
    private CardDeletionService cardDeletionService;
    private CardBlockRequestService cardBlockRequestService;

    private AdminCardController adminCardController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private FullCardRecordDTO fullCardRecordDTO = new FullCardRecordDTO(1,
            "**** **** **** 8888", "12/24",
            CardStatusEnum.ACTIVE.toString(), BigDecimal.ZERO);
    private SimpleCardRecordDTO simpleCardRecordDTO = new SimpleCardRecordDTO(1,
            "**** **** **** 8888", CardStatusEnum.ACTIVE.toString());
    private Map<String,String> mapResponse = new HashMap<>();

    @BeforeEach
    void setUp() {
        cardBlockRequestService = Mockito.mock(CardBlockRequestService.class);
        cardCreationService = Mockito.mock(CardCreationService.class);
        cardService = Mockito.mock(CardService.class);
        cardStatusService = Mockito.mock(CardStatusService.class);
        cardDeletionService = Mockito.mock(CardDeletionService.class);

        adminCardController = new AdminCardController(cardCreationService,
                cardService, cardStatusService, cardDeletionService, cardBlockRequestService);

        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(adminCardController).build();
    }

    @Test
    void createCard() throws Exception {

        when(cardCreationService.createCard(anyInt())).thenReturn(fullCardRecordDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/cards/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(fullCardRecordDTO.id()))
                .andExpect(jsonPath("$.meshCardNumber").value(fullCardRecordDTO.meshCardNumber()));

        verify(cardCreationService).createCard(anyInt());
    }

    @Test
    void blockCard() throws Exception {
        mapResponse.put("status", "BLOCKED");

        when(cardStatusService.blockCard(anyInt())).thenReturn(mapResponse);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/admin/cards/{userId}/block", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("BLOCKED"));

        verify(cardStatusService).blockCard(anyInt());
    }

    @Test
    void activateCard() throws Exception {
        mapResponse.put("status", "ACTIVE");

        when(cardStatusService.activateCard(anyInt())).thenReturn(mapResponse);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/admin/cards/{userId}/activate", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(cardStatusService).activateCard(anyInt());
    }

    @Test
    void deleteCard() throws Exception {

        doNothing().when(cardDeletionService).delete(anyInt());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/cards/{userId}", 1))
                .andExpect(status().isNoContent());

        verify(cardDeletionService).delete(1);
    }

    @Test
    void blockCardByBlockRequest() throws Exception {
        mapResponse.put("message","карта с id: 1 заблокирована");

        when(cardBlockRequestService.blockByRequest(anyInt())).thenReturn(mapResponse);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/cards/block-requests/{requestId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("карта с id: 1 заблокирована"));

        verify(cardBlockRequestService).blockByRequest(1);
    }
}
