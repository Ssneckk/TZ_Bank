package com.example.bankcards.service.user;

import java.util.Map;

/**
 * Сервис для блокировки и разблокировки пользователей.
 */
public interface BlockUserService {

   /**
    * Блокирует или разблокирует пользователя по идентификатору.
    *
    * @param userId идентификатор пользователя
    * @param blockOrNot {@code true} — блокировать, {@code false} — разблокировать
    * @return информация о результате операции в виде ключ-значение
    */
   Map<String, String> block(Integer userId, Boolean blockOrNot);
}

