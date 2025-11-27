package com.example.bankcards.service.transfer.impls;

import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.CardException;
import com.example.bankcards.exception.TransferException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.security.jwt.JwtProvider;
import com.example.bankcards.service.transfer.TransferService;
import com.example.bankcards.util.auxiliaryclasses.request.TransferRequest;
import com.example.bankcards.util.enums.CardStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class TransferServiceImpl implements TransferService {

    private final JwtProvider jwtProvider;
    private final CardRepository cardRepository;

    @Autowired
    public TransferServiceImpl(JwtProvider jwtProvider, CardRepository cardRepository) {
        this.jwtProvider = jwtProvider;
        this.cardRepository = cardRepository;
    }

    @Override
    @Transactional
    public Map<String, String> transferBetweenOwnCards(TransferRequest transferRequest, String token) {
        int userId = jwtProvider.extrackId(token);
        int cardFromId = transferRequest.getFromCardId();
        int cardToId = transferRequest.getToCardId();

        checkIsUserOwnerBoth(cardFromId, cardToId, userId);

        Card cardFrom = cardRepository.findById(cardFromId)
                .orElseThrow(()->new CardException("Карта FROM не найдена"));
        BigDecimal amount = transferRequest.getAmount();

        checkBalanceFromCard(cardFrom.getBalance(), amount);

        Card cardTo = cardRepository.findById(cardToId)
                .orElseThrow(()->new CardException("Карта TO не найдена"));

        checkCardsStatus(cardFrom, cardTo);

        cardFrom.setBalance(cardFrom.getBalance().subtract(amount));
        cardTo.setBalance(cardTo.getBalance().add(amount));

        Map<String,String> response = new HashMap<>();
        response.put("message", "Транзакция успешно завершена");

        return response;
    }

    protected void checkBalanceFromCard(BigDecimal cardsFromBalance, BigDecimal amountToTransfer) {
        if(cardsFromBalance.compareTo(amountToTransfer)<0){
            throw new TransferException("на карте недостаточно средств");
        }
    }

    protected void checkIsUserOwnerBoth(int card1, int card2, int userId) {
        boolean card1Exists = cardRepository.existsByIdAndUserId(card1, userId);
        boolean card2Exists = cardRepository.existsByIdAndUserId(card2, userId);

        if(!card1Exists || !card2Exists) {
            throw new TransferException("Одна из карт вам не принадлежит");
        }
    }

    protected void checkCardsStatus(Card fromCardId, Card toCardId) {

        CardStatusEnum cardStatusEnum = fromCardId.getStatus();
        CardStatusEnum toCardStatusEnum = toCardId.getStatus();

        if (!cardStatusEnum.equals(CardStatusEnum.ACTIVE)||!toCardStatusEnum.equals(CardStatusEnum.ACTIVE)) {
            throw new TransferException("Одна либо обе карты заблокированы");
        }
    }
}
