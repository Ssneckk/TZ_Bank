package com.example.bankcards.util.converters.impls;

import com.example.bankcards.dto.RoleDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.util.auxiliaryclasses.AuthAndRegistrRequest;
import com.example.bankcards.util.converters.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class UserConverterImpls implements UserConverter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserConverterImpls(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());

        //Безопасно конвертируем наш List<Role> в List<RoleDTO>
        List<RoleDTO> roleDTOs = user.getRoles() == null
                ? Collections.emptyList()
                : user.getRoles().stream().map(
                    this::convertToRoleDTO).toList();

        userDTO.setRoles(roleDTOs);
        return userDTO;
    }

    @Override
    public User convertToUser(AuthAndRegistrRequest authAndRegistrRequest) {
        User user = new User();
        user.setEmail(authAndRegistrRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authAndRegistrRequest.getPassword()));
        return user;
    }

    public RoleDTO convertToRoleDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRole_name(role.getRole_name());
        return roleDTO;
    }
}
