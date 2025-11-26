package com.example.bankcards.service.card;

import com.example.bankcards.dto.MyRecords;
import com.example.bankcards.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardService {

    Page<MyRecords.simpleCardRecordDTO> getCards(Pageable pageable);

    Page<MyRecords.simpleCardRecordDTO> getUsersCards(Pageable pageable, String authHeader);

    MyRecords.fullCardRecordDTO getCard(Integer cardId, String authHeader);

    Card findCardById(Integer cardId);
}
