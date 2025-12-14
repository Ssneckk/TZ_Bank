package com.example.bankcards.util.converters.impls;

import com.example.bankcards.dto.FullCardRecordDTO;
import com.example.bankcards.dto.SimpleCardRecordDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.util.converters.CardConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

/**
 * Конвертер сущности {@link Card} в DTO-объекты.
 *
 * <p>Предоставляет методы для преобразования карты в полное и упрощенное представление,
 * которое используется в ответах REST API.</p>
 */
@Component
public class CardConverterImpls implements CardConverter {

    private static final Logger log = LoggerFactory.getLogger(CardConverterImpls.class);

    /**
     * Преобразует {@link Card} в {@link FullCardRecordDTO}.
     *
     * @param card карта, которую нужно преобразовать
     * @return полное DTO с идентификатором, маскированным номером, датой истечения срока,
     * статусом и балансом
     */
    @Override
    public FullCardRecordDTO convertToFullCardRecord(Card card) {
        Integer id = card.getId();
        String meshCardNumber = "**** **** **** "+card.getCard_number_last4();
        String formattedDate = card.getExpireDate().format(
                DateTimeFormatter.ofPattern("MM/yy"));
        String status = String.valueOf(card.getStatus());
        BigDecimal balance = card.getBalance();

        log.debug("Конвертация карты в FullCardRecordDTO: id={}, meshCardNumber={}, status={}",
                id, meshCardNumber, status);

        return new FullCardRecordDTO(id,meshCardNumber,formattedDate,status,balance);
    }

    /**
     * Преобразует {@link Card} в {@link SimpleCardRecordDTO}.
     *
     * @param card карта, которую нужно преобразовать
     * @return упрощенное DTO с идентфикатором, маскированным номером и статусом
     */
    @Override
    public SimpleCardRecordDTO convertToSimpleCardRecord(Card card) {
        Integer cardId = card.getId();
        String meshCardNumber = "**** **** **** "+card.getCard_number_last4();
        String status = String.valueOf(card.getStatus());

        log.debug("Конвертация карты в SimpleCardRecordDTO: id={}, meshCardNumber={}, status={}",
                cardId, meshCardNumber, status);

        return new SimpleCardRecordDTO(cardId, meshCardNumber, status);
    }
}
