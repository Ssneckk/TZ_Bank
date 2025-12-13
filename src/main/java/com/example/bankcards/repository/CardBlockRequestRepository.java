package com.example.bankcards.repository;

import com.example.bankcards.entity.CardBlockRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с запросами на блокировку карт.
 * <p>Позволяет сохранять, получать, удалять и искать запросы по идентификатору карты.</p>
 */
@Repository
public interface CardBlockRequestRepository extends JpaRepository<CardBlockRequest, Integer> {

    /**
     * Поиск запроса на блокировку по идентификатору карты.
     * @param cardId идентификатор карты
     * @return {@link Optional} с найденным запросом или пустой, если не найден
     */
    Optional<CardBlockRequest> findByCardId(Integer cardId);
}

