package com.example.bankcards.repository;

import com.example.bankcards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы c пользователями.
 * Позволяет сохранять, получать по идентификатору,
 * удалять и обновлять пользователей.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Ищет пользователя по его электронной почте.
     *
     * @param email электронная почта пользователя
     * @return {@link Optional} с {@link User}, если пользователь найден, иначе {@link Optional#empty()}
     */
    Optional<User> findByEmail(String email);


    /**
     * Получает страницу пользователей с подгрузкой их ролей.
     *
     * @param pageable параметры пагинации и сортировки
     * @return {@link Page} содержащая {@link User} с загруженными {@code roles}
     */
    @EntityGraph(attributePaths = "roles")
    Page<User> findAll(Pageable pageable);

}
