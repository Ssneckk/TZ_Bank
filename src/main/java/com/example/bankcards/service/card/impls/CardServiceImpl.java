package com.example.bankcards.service.card.impls;

import com.example.bankcards.dto.FullCardRecordDTO;
import com.example.bankcards.dto.SimpleCardRecordDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.CardException;
import com.example.bankcards.exception.UserException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.jwt.JwtProvider;
import com.example.bankcards.service.card.CardService;
import com.example.bankcards.util.converters.CardConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardConverter cardConverter;
    private final JwtProvider jwtProvider;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository, CardConverter cardConverter, JwtProvider jwtProvider) {
        this.cardRepository = cardRepository;
        this.cardConverter = cardConverter;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Page<SimpleCardRecordDTO> getCards(Pageable pageable) {
        return cardRepository.findAll(pageable)
                .map(cardConverter::convertToSimpleCardRecord);
    }

    @Override
    public Page<SimpleCardRecordDTO> getUsersCards(Pageable pageable, String authHeader) {
        int userId = jwtProvider.extrackId(authHeader);
        return cardRepository.findByUserId(userId, pageable)
                .map(cardConverter::convertToSimpleCardRecord);
    }

    @Override
    public FullCardRecordDTO getCard(Integer cardId, String authHeader) {
        int userId = jwtProvider.extrackId(authHeader);

        Card usersCard = findCardById(cardId);

        boolean exists = cardRepository.existsByIdAndUserId(cardId, userId);

        if(!exists) {
            throw new UserException("Карта не найдена, проверьте свои карты");
        }

        return cardConverter.convertToFullCardRecord(usersCard);
    }

    @Override
    public Card findCardById(Integer cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(()-> new CardException("Карта с id: "
                        + cardId + " не найдена"));
    }
}
