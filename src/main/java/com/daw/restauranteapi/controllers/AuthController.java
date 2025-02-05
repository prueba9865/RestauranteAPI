package com.daw.restauranteapi.controllers;

import com.daw.restauranteapi.DTO.LoginRequestDTO;
import com.daw.restauranteapi.DTO.LoginResponseDTO;
import com.daw.restauranteapi.DTO.UserRegisterDTO;
import com.daw.restauranteapi.config.JwtTokenProvider;
import com.daw.restauranteapi.entities.UserEntity;
import com.daw.restauranteapi.repositories.UserEntityRepository;
import com.daw.restauranteapi.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AuthController {
    @Autowired
    private UserEntityRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthService authService;

    @PostMapping("/auth/register")
    public ResponseEntity<Map<String,String>> save(@RequestBody UserRegisterDTO userDTO) {
        return authService.registrarUsuario(userDTO);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginDTO) {
        return authService.logeoUsuario(loginDTO);
    }
    @PutMapping("/auth/asignarRol/{id}")
    public ResponseEntity<?> asignarRol(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        return authService.asignarRolAdmin(id, authHeader);
    }

}
