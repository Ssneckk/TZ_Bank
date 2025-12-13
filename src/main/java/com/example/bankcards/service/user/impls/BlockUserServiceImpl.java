package com.example.bankcards.service.user.impls;

import com.example.bankcards.entity.User;
import com.example.bankcards.exception.exceptions.UserException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.user.BlockUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class BlockUserServiceImpl implements BlockUserService {

    private final UserRepository userRepository;

    public BlockUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Map<String, String> block(Integer user_id , Boolean blockOrNot) {

        User user = findById(user_id);

        user.setBlocked(blockOrNot);

        userRepository.save(user);

        //Можно сделать кастомный ответ, но пока так
        Map<String, String> response = new HashMap<>();

        if (blockOrNot) {
            response.put("message", "Пользователь с ID: " + user_id + " заблокирован");
            return response;
        }
        else {
            response.put("message","Пользователь с ID: " + user_id + " разблокирован");
            return response;
        }
    }

    private User findById(Integer user_id) {
        return userRepository.findById(user_id)
                .orElseThrow(() -> new UserException("User с id: " + user_id + " не найден"));
    }
}
