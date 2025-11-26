package com.example.bankcards.service.user.impls;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.TokenException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.user.UserService;
import com.example.bankcards.util.converters.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    @Override
    public UserDTO getInfo(Integer user_id) {

        User user = userRepository.findById(user_id)
                .orElseThrow(()-> new TokenException("User с id из токена: "
                        + user_id + " не найден"));

        return userConverter.convertToDTO(user);
    }

    //N+1 проблема здесь (Решено)
    @Override
    public List<UserDTO> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userConverter::convertToDTO).toList();
    }
}
