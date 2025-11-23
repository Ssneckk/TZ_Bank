package com.example.bankcards.service.impls;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.UserAlreadyExistsException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.auxiliaryclasses.AuthAndRegistrRequest;
import com.example.bankcards.util.converters.UserConverter;
import com.example.bankcards.service.RegisterService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RegisterServiceImpls implements RegisterService {

    private final UserConverter userConverter;
    private final UserRepository userRepository;

    public RegisterServiceImpls(UserConverter userConverter, UserRepository userRepository) {
        this.userConverter = userConverter;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDTO registr(AuthAndRegistrRequest authAndRegistrRequest) {

        User user = userConverter.convertToUser(authAndRegistrRequest);

        if(userRepository.findByEmail(authAndRegistrRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User c таким Email: "
                    + authAndRegistrRequest.getEmail() + " уже существует");
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
