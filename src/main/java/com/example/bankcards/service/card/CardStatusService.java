package com.example.bankcards.service.card;

import java.util.Map;

public interface CardStatusService {

    void setCardsExpired();

    Map<String,String> blockCard(Integer card_id);

    Map<String,String> activateCard(Integer card_id);
}
