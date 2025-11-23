package com.example.bankcards.service.impls;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.TokenException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.jwt.JwtProvider;
import com.example.bankcards.service.UserInfoService;
import com.example.bankcards.util.converters.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Autowired
    public UserInfoServiceImpl(JwtProvider jwtProvider, UserRepository userRepository, UserConverter userConverter) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    @Override
    public UserDTO getInfo(String authHeader) {
        int user_id = jwtProvider.extrackId(authHeader);

        User user = userRepository.findById(user_id)
                .orElseThrow(()-> new TokenException("User с id из токена: "
                        + user_id + " не найден"));

        return userConverter.convertToDTO(user);
    }
}
