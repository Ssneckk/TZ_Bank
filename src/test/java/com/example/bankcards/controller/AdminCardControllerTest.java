package com.example.bankcards.controller;

import com.example.bankcards.dto.FullCardRecordDTO;
import com.example.bankcards.dto.SimpleCardRecordDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.service.card.*;
import com.example.bankcards.util.enums.CardStatusEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
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

    private FullCardRecordDTO fullCardRecordDTO = new FullCardRecordDTO(1,
            "**** **** **** 8888", "12/24",
            CardStatusEnum.ACTIVE.toString(), BigDecimal.ZERO);

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

        mockMvc = MockMvcBuilders.standaloneSetup(adminCardController).build();
    }

    @Test
    void getCards_shouldReturnAllCards_directly() {
        List<SimpleCardRecordDTO> simpleCardRecordDTOS
                = List.of(new SimpleCardRecordDTO(1, "1", "active"),
                new SimpleCardRecordDTO(2, "2", "blocked"));

        Page<SimpleCardRecordDTO> simpleCardRecordDTOPage =
                new PageImpl<>(simpleCardRecordDTOS);

        when(cardService.getCards(any(Pageable.class))).thenReturn(simpleCardRecordDTOPage);

       Page<SimpleCardRecordDTO> result =
               adminCardController.getCards(PageRequest.of(0, 10)).getBody();

        assertEquals(2, result.getContent().size());
        assertEquals("1", result.getContent().get(0).meshCardNumber());
        assertEquals("active", result.getContent().get(0).status());
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
    void getRequests_shouldReturnAllRequests_directly() {

        Page<CardBlockRequest> requestPage = new PageImpl<>(List.of(new CardBlockRequest(),
                new CardBlockRequest()));

        when(cardBlockRequestService.getRequests(any(Pageable.class))).thenReturn(requestPage);

        Page<CardBlockRequest> result = adminCardController.getRequests(PageRequest.of(0, 10)).getBody();

        Assertions.assertEquals(2, result.getContent().size());
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
