package com.example.bankcards.service.user;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserDTO getInfo(Integer user_id);

    Page<UserDTO> getUsers(Pageable pageable);

    int getCurrentUserId();

    User findCurrentUser();

}
