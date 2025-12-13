package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * Репозиторий для работы с картами пользователей.
 * Позволяет сохранять, получать по идентификатору,
 * удалять и обновлять карты пользователей.
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {

    /**
     * Помечает карты как просроченные, если дата их окончания меньше указанной.
     *
     * @param date дата, до которой карты считаются действительными
     * @return количество карт, которые были помечены как просроченные
     */
    @Modifying
    @Query("UPDATE Card c SET c.status = 'EXPIRED' WHERE c.expireDate< :date")
    int markExpired(@Param("date") LocalDate date);

    /**
     * Возвращает страницу карт пользователя по идентификатору пользователя с
     * возможностью пагинации и сортировки
     * @param userId - идентификатор пользователя
     * @param pageable - параметры пагинации и сортировки
     * @return {@link Page} страница, содержащая {@link Card} карты пользователя
     */
    Page<Card> findByUserId(Integer userId, Pageable pageable);

    /**
     * Проверяет, существует ли карта с указанным идентификатором карты и
     * принадлежит ли она указанному пользователю.
     *
     * @param cardId идентификатор карты
     * @param userId идентификатор пользователя
     * @return {@code true}, если карта с таким идентификатором принадлежит пользователю,
     * иначе {@code false}
     */
    boolean existsByIdAndUserId(Integer cardId, Integer userId);
}
