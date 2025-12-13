package com.example.bankcards.service.card;

import com.example.bankcards.dto.FullCardRecordDTO;
import com.example.bankcards.dto.SimpleCardRecordDTO;
import com.example.bankcards.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardService {

    Page<SimpleCardRecordDTO> getCards(Pageable pageable);

    Page<SimpleCardRecordDTO> getUsersCards(Pageable pageable);

    FullCardRecordDTO getCard(Integer cardId);

    Card findCardById(Integer cardId);
}
