package com.example.bankcards.service.user.impls;

import com.example.bankcards.entity.User;
import com.example.bankcards.exception.exceptions.UserException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.user.BlockUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Сервис для блокировки и разблокировки пользователей.
 *
 * <p>Отвечает за изменение статуса блокировки пользователя
 * и сохранение изменений в базе данных.</p>
 */
@Service
public class BlockUserServiceImpl implements BlockUserService {

    private static final Logger log = LoggerFactory.getLogger(BlockUserServiceImpl.class);

    private final UserRepository userRepository;

    public BlockUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Блокирует или разблокирует пользователя по идентификатору.
     *
     * @param userId    идентификатор пользователя
     * @param blockOrNot {@code true} — заблокировать пользователя,
     *                   {@code false} — разблокировать
     * @return {@link Map} с сообщением о результате операции
     * @throws UserException если пользователь не найден
     */
    @Override
    @Transactional
    public Map<String, String> block(Integer userId , Boolean blockOrNot) {
        log.info("Начало операции {} пользователя с ID: {}", blockOrNot ? "блокировки" : "разблокировки", userId);

        User user = findUserById(userId);

        user.setBlocked(blockOrNot);
        userRepository.save(user);

        //Можно сделать кастомный ответ, но пока так
        Map<String, String> response = new HashMap<>();

        if (blockOrNot) {
            response.put("message", "Пользователь с ID: " + userId + " заблокирован");
            log.info("Пользователь с ID: {} успешно заблокирован", userId);
        }
        else {
            response.put("message","Пользователь с ID: " + userId + " разблокирован");
            log.info("Пользователь с ID: {} успешно разблокирован", userId);
        }

        return response;
    }

    private User findUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Пользователь с ID: {} не найден", userId);
                    return new UserException("User с id: " + userId + " не найден");
                });
    }
}
