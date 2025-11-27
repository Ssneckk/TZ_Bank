package com.example.bankcards.service.card;

import com.example.bankcards.dto.FullCardRecordDTO;

public interface CardCreationService {

    FullCardRecordDTO createCard(Integer id) throws Exception;
}
