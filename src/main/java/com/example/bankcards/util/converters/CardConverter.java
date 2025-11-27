package com.example.bankcards.util.converters;

import com.example.bankcards.dto.FullCardRecordDTO;
import com.example.bankcards.dto.SimpleCardRecordDTO;
import com.example.bankcards.entity.Card;

public interface CardConverter {

    FullCardRecordDTO convertToFullCardRecord(Card card);

    SimpleCardRecordDTO convertToSimpleCardRecord(Card card);
}
