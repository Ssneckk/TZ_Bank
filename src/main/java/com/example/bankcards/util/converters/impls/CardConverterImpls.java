package com.example.bankcards.util.converters.impls;

import com.example.bankcards.dto.MyRecords;
import com.example.bankcards.entity.Card;
import com.example.bankcards.util.converters.CardConverter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Component
public class CardConverterImpls implements CardConverter {

    @Override
    public MyRecords.fullCardRecordDTO convertToFullCardRecord(Card card) {
        Integer id = card.getId();
        String meshCardNumber = "**** **** **** "+card.getCard_number_last4();
        String formattedDate = card.getExpireDate().format(
                DateTimeFormatter.ofPattern("MM/yy"));
        String status = String.valueOf(card.getStatus());
        BigDecimal balance = card.getBalance();

        return new MyRecords.fullCardRecordDTO(id,meshCardNumber,formattedDate,status,balance);
    }

    @Override
    public MyRecords.simpleCardRecordDTO convertToSimpleCardRecord(Card card) {
        Integer cardId = card.getId();
        String meshCardNumber = "**** **** **** "+card.getCard_number_last4();
        String status = String.valueOf(card.getStatus());

        return new MyRecords.simpleCardRecordDTO(cardId, meshCardNumber, status);
    }
}
