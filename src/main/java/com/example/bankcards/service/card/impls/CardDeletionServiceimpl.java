package com.example.bankcards.service.card.impls;

import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.exceptions.CardException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.card.CardDeletionService;
import com.example.bankcards.util.enums.CardStatusEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CardDeletionServiceimpl implements CardDeletionService {

    private final CardRepository cardRepository;

    public CardDeletionServiceimpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    //Решил реализовать удаление так, потому что на данный момент не знаю нужно ли
    //сохранять транзакции, пока делаю без транзакций
    @Override
    @Transactional
    public void delete(Integer cardId) {
        Card card = findCard(cardId);

        if(card.getBalance().compareTo(BigDecimal.ZERO)!=0){
            throw new CardException("Баланс на карте не равен 0");
        }

        card.setStatus(CardStatusEnum.DELETED);
    }

    private Card findCard(Integer cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(()-> new CardException("Карта с id: "
                        + cardId + " не найдена"));
    }
}
