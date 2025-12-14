package com.example.bankcards.controller;

import com.example.bankcards.dto.RoleDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.service.user.BlockUserService;
import com.example.bankcards.service.user.RegisterService;
import com.example.bankcards.service.user.UserService;
import com.example.bankcards.util.auxiliaryclasses.request.AuthAndRegisterRequest;
import com.example.bankcards.util.enums.RoleEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.mapping.Collection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminUserControllerTest {

    private MockMvc mockMvc;
    private RegisterService registerService;
    private UserService userService;
    private BlockUserService blockUserService;
    private ObjectMapper objectMapper;
    private AdminUserController adminUserController;

    private UserDTO userDTO =
            new UserDTO(1, "test@email.com", false,List.of());

    private AuthAndRegisterRequest registerRequest =
            new AuthAndRegisterRequest("test@email.com", "password");

    @BeforeEach
    void setUp() {
        registerService = Mockito.mock(RegisterService.class);
        userService = Mockito.mock(UserService.class);
        blockUserService = Mockito.mock(BlockUserService.class);
        objectMapper = new ObjectMapper();

        adminUserController = new AdminUserController(registerService, userService, blockUserService);

        mockMvc = MockMvcBuilders.standaloneSetup(adminUserController).build();
    }

    @Test
    void register_shouldReturnUserDTO() throws Exception {
        when(registerService.registr(any(AuthAndRegisterRequest.class))).thenReturn(userDTO);

        mockMvc.perform(post("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()));

        verify(registerService).registr(any(AuthAndRegisterRequest.class));
    }

    @Test
    void getUsers_shouldReturnUserDTOs(){

        Page<UserDTO> userDTOPage = new PageImpl<>(Collections.singletonList(userDTO));

        when(userService.getUsers(any(Pageable.class))).thenReturn(userDTOPage);

        Page<UserDTO> result =
                adminUserController.getUsers(PageRequest.of(0, 10)).getBody();

        Assertions.assertEquals(1, result.getContent().size());
        Assertions.assertEquals(userDTO, result.getContent().get(0));
    }

    @Test
    void getAccountInfo_shouldReturnUserDTO() throws Exception {
        when(userService.getInfo(anyInt())).thenReturn(userDTO);

        mockMvc.perform(get("/api/admin/users/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()));

        verify(userService).getInfo(1);
    }

    @Test
    void block_shouldReturnMap() throws Exception {
        Map<String, String> response = Map.of("status", "blocked");
        when(blockUserService.block(anyInt(), anyBoolean())).thenReturn(response);

        mockMvc.perform(patch("/api/admin/users/{userId}/block", 1)
                        .param("blockOrNot", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("blocked"));

        verify(blockUserService).block(1, true);
    }
}
