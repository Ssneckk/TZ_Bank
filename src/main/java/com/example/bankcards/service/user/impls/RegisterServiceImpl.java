package com.example.bankcards.service.user.impls;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.exceptions.UserException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.auxiliaryclasses.request.AuthAndRegisterRequest;
import com.example.bankcards.util.converters.UserConverter;
import com.example.bankcards.service.user.RegisterService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RegisterServiceImpl implements RegisterService {

    private final UserConverter userConverter;
    private final UserRepository userRepository;

    public RegisterServiceImpl(UserConverter userConverter, UserRepository userRepository) {
        this.userConverter = userConverter;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDTO registr(AuthAndRegisterRequest authAndRegisterRequest) {

        User user = userConverter.convertToEntity(authAndRegisterRequest);

        if(userRepository.findByEmail(authAndRegisterRequest.getEmail()).isPresent()) {
            throw new UserException("User c таким Email: "
                    + authAndRegisterRequest.getEmail() + " уже существует");
        }

        User newUser = userRepository.save(user);

        Role role = new Role();

        List<Role> userRole = new ArrayList<>();

        userRole.add(role);

        role.setUser(newUser);

        newUser.setRoles(userRole);

        return userConverter.convertToDTO(newUser);
    }
}
