package com.example.bankcards.controller;

import com.example.bankcards.dto.FullCardRecordDTO;
import com.example.bankcards.dto.SimpleCardRecordDTO;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.service.card.CardBlockRequestService;
import com.example.bankcards.service.card.CardService;
import com.example.bankcards.service.transfer.TransferService;
import com.example.bankcards.util.auxiliaryclasses.request.TransferRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    public final CardService cardService;
    public final TransferService transferService;
    public final CardBlockRequestService cardBlockRequestService;

    @Autowired
    public CardController(CardService cardService, TransferService transferService, CardBlockRequestService cardBlockRequestService) {
        this.cardService = cardService;
        this.transferService = transferService;
        this.cardBlockRequestService = cardBlockRequestService;
    }

    @GetMapping
    public ResponseEntity<Page<SimpleCardRecordDTO>> getUsersCards(@PageableDefault(size = 10) Pageable pageable,
                                                                   @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(cardService.getUsersCards(pageable, authHeader));
    }

    @GetMapping("/{cardId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<FullCardRecordDTO> getCard(@PathVariable Integer cardId,
                                                     @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(cardService.getCard(cardId, authHeader));
    }

    @PostMapping("/{cardId}/block-request")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<CardBlockRequest> makeCardBlockRequest(@PathVariable Integer cardId,
                                                                 @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(cardBlockRequestService.makeRequest(cardId, authHeader));
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Map<String, String>> transfer(@RequestBody @Valid TransferRequest transferRequest,
                                                        @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(transferService.transferBetweenOwnCards(transferRequest,authHeader));
    }



}
