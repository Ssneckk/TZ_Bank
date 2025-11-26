package com.example.bankcards.controller;

import com.example.bankcards.dto.MyRecords;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.service.card.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/cards")
public class AdminCardController {

    private final CardCreationService cardCreationService;
    private final CardService cardService;
    private final CardStatusService cardStatusService;
    private final CardDeletionService cardDeletionService;
    private final CardBlockRequestService cardBlockRequestService;

    @Autowired
    public AdminCardController(CardCreationService cardCreationService, CardService cardService, CardStatusService cardStatusService, CardDeletionService cardDeletionService, CardBlockRequestService cardBlockRequestService) {
        this.cardCreationService = cardCreationService;
        this.cardService = cardService;
        this.cardStatusService = cardStatusService;
        this.cardDeletionService = cardDeletionService;
        this.cardBlockRequestService = cardBlockRequestService;
    }

    @PostMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MyRecords.fullCardRecordDTO> createCard(@PathVariable Integer userId) throws Exception{
        return ResponseEntity.ok(cardCreationService.createCard(userId));
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<MyRecords.simpleCardRecordDTO>> getCards(Pageable pageable) {
        return ResponseEntity.ok(cardService.getCards(pageable));
    }

    @PatchMapping("/{cardId}/block")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> blockCard(@PathVariable Integer cardId) {
        return ResponseEntity.ok(cardStatusService.blockCard(cardId));
    }

    @PatchMapping("/{cardId}/activate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> activateCard(@PathVariable Integer cardId) {
        return ResponseEntity.ok(cardStatusService.activateCard(cardId));
    }

    @DeleteMapping("/{cardId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteCard(@PathVariable Integer cardId) {
        cardDeletionService.delete(cardId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/block-requests")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<CardBlockRequest>> getRequests(Pageable pageable) {
        return ResponseEntity.ok(cardBlockRequestService.getRequests(pageable));
    }

    @DeleteMapping("/block-requests/{requestId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> blockCardByRequest(@PathVariable Integer requestId) {
        return ResponseEntity.ok(cardBlockRequestService.blockByRequest(requestId));
    }
}
