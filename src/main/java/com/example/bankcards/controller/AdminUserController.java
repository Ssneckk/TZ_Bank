package com.example.bankcards.controller;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.service.user.BlockUserService;
import com.example.bankcards.service.user.RegisterService;
import com.example.bankcards.service.user.UserService;
import com.example.bankcards.util.auxiliaryclasses.request.AuthAndRegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final RegisterService registerService;
    private final UserService userService;
    private final BlockUserService blockUserService;

    @Autowired
    public AdminUserController(RegisterService registerService, UserService userService, BlockUserService blockUserService) {
        this.registerService = registerService;
        this.userService = userService;
        this.blockUserService = blockUserService;
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody AuthAndRegisterRequest authAndRegisterRequest) {
        return ResponseEntity.ok(registerService.registr(authAndRegisterRequest));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getAccountInfo(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.getInfo(userId));
    }

    @PatchMapping("/{userId}/block")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String,String>> block(@PathVariable Integer userId, @RequestParam Boolean blockOrNot) {
        return ResponseEntity.ok(blockUserService.block(userId, blockOrNot));
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDTO>> getUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getUsers(pageable));
    }

}
