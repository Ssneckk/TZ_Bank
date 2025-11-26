package com.example.bankcards.service.card.impls;

import com.example.bankcards.dto.MyRecords;
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
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository, CardConverter cardConverter, UserRepository userRepository, JwtProvider jwtProvider) {
        this.cardRepository = cardRepository;
        this.cardConverter = cardConverter;
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Page<MyRecords.simpleCardRecordDTO> getCards(Pageable pageable) {
        return cardRepository.findAll(pageable)
                .map(cardConverter::convertToSimpleCardRecord);
    }

    @Override
    public Page<MyRecords.simpleCardRecordDTO> getUsersCards(Pageable pageable, String authHeader) {
        int userId = jwtProvider.extrackId(authHeader);
        return cardRepository.findByUserId(userId,pageable)
                .map(cardConverter::convertToSimpleCardRecord);
    }

    @Override
    public MyRecords.fullCardRecordDTO getCard(Integer cardId, String authHeader) {
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
