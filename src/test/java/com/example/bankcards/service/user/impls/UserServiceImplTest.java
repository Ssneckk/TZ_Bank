package com.example.bankcards.service.user.impls;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.exceptions.UserException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.converters.UserConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserRepository userRepository;
    private UserConverter userConverter;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userConverter = Mockito.mock(UserConverter.class);
        userService = new UserServiceImpl(userRepository, userConverter);
    }

    @Test
    void getInfo_shouldReturnUserDTO() {
        int userId = 1;
        User user = new User();
        UserDTO userDTO = new UserDTO();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userConverter.convertToDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getInfo(userId);

        assertEquals(userDTO, result);
        verify(userRepository).findById(userId);
        verify(userConverter).convertToDTO(user);
    }

    @Test
    void getInfo_shouldThrowTokenException() {
        int userId = 2;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserException exception = assertThrows(UserException.class, () -> userService.getInfo(userId));
        assertEquals("User с id: 2 не найден", exception.getMessage());
    }

//    @Test
//    void getUsers_shouldReturnListOfUserDTO() {
//        PageRequest pageable = PageRequest.of(0, 10);
//        User user = new User();
//        UserDTO userDTO = new UserDTO();
//
//        Page<User> userPage = new PageImpl<>(List.of(user));
//        when(userRepository.findAll(pageable)).thenReturn(userPage);
//        when(userConverter.convertToDTO(user)).thenReturn(userDTO);
//
//        Page<UserDTO> result = userService.getUsers(pageable);
//
//        assertEquals(1, result.size());
//        assertEquals(userDTO, result.get(0));
//        verify(userRepository).findAll(pageable);
//        verify(userConverter).convertToDTO(user);
//    }
}
