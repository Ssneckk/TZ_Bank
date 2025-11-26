package com.example.bankcards.service.card;

import com.example.bankcards.dto.MyRecords;

public interface CardCreationService {

    MyRecords.fullCardRecordDTO createCard(Integer id) throws Exception;
}
