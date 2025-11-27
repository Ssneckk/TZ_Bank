package com.example.bankcards.service.card.impls;

import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.CardException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.card.CardService;
import com.example.bankcards.service.card.CardStatusService;
import com.example.bankcards.util.enums.CardStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class CardStatusServiceImpl implements CardStatusService {

    private static final Logger log = LoggerFactory.getLogger(CardStatusServiceImpl.class);
    private final CardRepository cardRepository;
    private final CardService cardService;

    @Autowired
    public CardStatusServiceImpl(CardRepository cardRepository, CardService cardService) {
        this.cardRepository = cardRepository;
        this.cardService = cardService;
    }

    @Override
    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    @Transactional
    public void setCardsExpired() {
        int e = cardRepository.markExpired(LocalDate.now());
        log.info(e + " Cards expired");
    }

    @Override
    @Transactional
    public Map<String, String> blockCard(Integer card_id) {
        Map<String, String> response = new HashMap<>();

        Card card = cardService.findCardById(card_id);

        CardStatusEnum cardStatus = card.getStatus();

        if (cardStatus == CardStatusEnum.BLOCKED || cardStatus == CardStatusEnum.DELETED) {
            throw new CardException("Карта уже заблокирована либо удалена");
        }

        card.setStatus(CardStatusEnum.BLOCKED);

        response.put("message of block: ", "Карта с id: " + card_id+" заблокирована");

        return response;
    }

    @Override
    @Transactional
    public Map<String, String> activateCard(Integer card_id) {
        Map<String, String> response = new HashMap<>();

        Card card = cardService.findCardById(card_id);

        CardStatusEnum cardStatus = card.getStatus();

        if (cardStatus == CardStatusEnum.DELETED) {
            throw new CardException("Карта удалена");
        }

        card.setStatus(CardStatusEnum.ACTIVE);

        response.put("message of activate: ", "Карта с id: " + card_id+" активирована");

        return response;
    }
}
