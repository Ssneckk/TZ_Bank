package com.example.bankcards.service.user.impls;

import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.user.BlockUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BlockUserServiceImplTest {

    private UserRepository userRepository;

    private BlockUserService blockUserService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        blockUserService = new BlockUserServiceImpl(userRepository);
    }

    @Test
    void block_checkTrue(){

        boolean block = true;
        User user = new User();
        user.setId(1);
        String message = "Пользователь с ID: " + user.getId() + " заблокирован";

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Map<String,String> map = blockUserService.block(user.getId(), block);

        assertEquals(message,map.get("message"));
        verify(userRepository).save(user);
    }

    @Test
    void block_checkFalse() {
        boolean block = false;
        User user = new User();
        user.setId(2);
        String message = "Пользователь с ID: " + user.getId() + " разблокирован";

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        Map<String,String> map = blockUserService.block(user.getId(), block);

        assertEquals(message, map.get("message"));
        verify(userRepository).save(user);
    }
}