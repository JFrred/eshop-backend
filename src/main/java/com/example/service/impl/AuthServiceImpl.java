package com.example.service.impl;

import com.example.dto.AuthenticationResponse;
import com.example.dto.LoginRequest;
import com.example.dto.RegisterRequest;
import com.example.mapper.UserViewMapper;
import com.example.model.*;
import com.example.repository.UserRepository;
import com.example.security.JwtTokenService;
import com.example.service.AuthService;
import com.example.service.MessageSender;
import com.example.validation.RegisterValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.CharBuffer;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final MessageSender messageSender;
    private final RegisterValidator registerValidator;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserViewMapper userViewMapper;

    @Override
    @Transactional
    public void register(RegisterRequest registerRequest) {
        registerValidator.validateRequest(registerRequest);
        User user = userViewMapper.mapRegisterRequestToUser(registerRequest);

        userRepository.save(user);

        String token = jwtTokenService.generateAccountActivationToken(user);
        messageSender.sendMessage(new EmailSender.ActivationEmail(user.getEmail(), token));
    }

    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                CharBuffer.wrap(loginRequest.getPassword())
                        ));

        User principal = (User) authenticate.getPrincipal();
        String token = jwtTokenService.generateAccessToken(principal);

        return new AuthenticationResponse(token, userViewMapper.mapToView(principal));
    }

    @Override
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

}