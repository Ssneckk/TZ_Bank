package com.example.bankcards.service.user.impls;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.exceptions.UserException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.user.RegisterService;
import com.example.bankcards.util.auxiliaryclasses.request.AuthAndRegisterRequest;
import com.example.bankcards.util.converters.UserConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegisterServiceImplTest {

    private UserConverter userConverter;
    private UserRepository userRepository;
    private RegisterService registerService;

    @BeforeEach
    void setUp() {
        userConverter = Mockito.mock(UserConverter.class);
        userRepository = Mockito.mock(UserRepository.class);
        registerService = new RegisterServiceImpl(userConverter, userRepository);
    }

    @Test
    void register_shouldThrowUserException() {

        AuthAndRegisterRequest authAndRegisterRequest = new AuthAndRegisterRequest();
        authAndRegisterRequest.setEmail("email@email.com");

        User user = new User();

        when(userRepository.findByEmail(authAndRegisterRequest.getEmail()))
                .thenReturn(Optional.of(user));

        UserException userException = assertThrows(UserException.class,
                ()-> registerService.registr(authAndRegisterRequest));

        String expectedMessage = "User c таким Email: "+
                authAndRegisterRequest.getEmail()+" уже существует";

        assertEquals(expectedMessage, userException.getMessage());
    }

    @Test
    void register_shouldReturnDTO() {
        AuthAndRegisterRequest request = new AuthAndRegisterRequest();
        request.setEmail("email@email.com");
        request.setPassword("password");

        User userEntity = new User();
        User savedUserEntity = new User();
        savedUserEntity.setId(1);

        UserDTO expectedDTO = new UserDTO();
        expectedDTO.setId(1);

        when(userConverter.convertToEntity(request)).thenReturn(userEntity);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(userEntity)).thenReturn(savedUserEntity);
        when(userConverter.convertToDTO(savedUserEntity)).thenReturn(expectedDTO);

        UserDTO result = registerService.registr(request);

        assertEquals(expectedDTO, result);
        verify(userConverter).convertToEntity(request);
        verify(userRepository).findByEmail(request.getEmail());
        verify(userRepository).save(userEntity);
    }
}