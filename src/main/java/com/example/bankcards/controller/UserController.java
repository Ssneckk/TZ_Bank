package com.example.bankcards.controller;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.service.LoginService;
import com.example.bankcards.service.RegisterService;
import com.example.bankcards.service.UserInfoService;
import com.example.bankcards.util.auxiliaryclasses.AuthAndRegistrRequest;
import com.example.bankcards.util.auxiliaryclasses.AuthResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final LoginService loginService;
    private final RegisterService registerService;
    private final UserInfoService userInfoService;

    @Autowired
    public UserController(LoginService loginService, RegisterService registerService, UserInfoService userInfoService) {
        this.loginService = loginService;
        this.registerService = registerService;
        this.userInfoService = userInfoService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthAndRegistrRequest authAndRegistrRequest) {
       return ResponseEntity.ok(loginService.authenticate(authAndRegistrRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody AuthAndRegistrRequest authAndRegistrRequest) {
        return ResponseEntity.ok(registerService.registr(authAndRegistrRequest));
    }

    @GetMapping("/account-info")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<UserDTO> getAccountInfo(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(userInfoService.getInfo(authHeader));
    }

    @GetMapping("/check-role-admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> getChaeckRoleAdmin() {
        return ResponseEntity.ok("Hello Admin");
    }


}
