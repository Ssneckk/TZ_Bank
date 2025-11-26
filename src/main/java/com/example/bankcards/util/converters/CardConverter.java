package com.example.bankcards.util.converters;

import com.example.bankcards.dto.MyRecords;
import com.example.bankcards.entity.Card;

public interface CardConverter {

    MyRecords.fullCardRecordDTO convertToFullCardRecord(Card card);

    MyRecords.simpleCardRecordDTO convertToSimpleCardRecord(Card card);
}
