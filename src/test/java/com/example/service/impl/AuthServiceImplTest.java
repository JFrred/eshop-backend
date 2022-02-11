package com.example.service.impl;

import com.example.dto.RegisterRequest;
import com.example.exception.InvalidPasswordException;
import com.example.exception.UsernameTakenException;
import com.example.mapper.UserViewMapper;
import com.example.model.User;
import com.example.repository.CartRepository;
import com.example.repository.UserRepository;
import com.example.security.JwtTokenService;
import com.example.service.MessageSender;
import com.example.validation.RegisterValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private MessageSender messageSender;
    @Mock
    private RegisterValidator registerValidator;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenService jwtTokenService;
    @Mock
    private UserViewMapper userViewMapper;

    private AuthServiceImpl authService;

    @Mock
    private User user;

    @BeforeEach
    void setup() {
        authService = new AuthServiceImpl(userRepository, cartRepository, messageSender,
                registerValidator, authenticationManager, jwtTokenService, userViewMapper);
    }

    @Test
    void registerWithValidRequest_ThenOK() {
        RegisterRequest request = new RegisterRequest();
        given(userViewMapper.mapRegisterRequestToUser(request)).willReturn(user);
        given(userRepository.save(any())).willReturn(user);

        authService.register(request);

        verify(registerValidator).validateRequest(request);
        verify(userViewMapper).mapRegisterRequestToUser(request);
        verify(jwtTokenService).generateAccountActivationToken(user);
        verify(messageSender).sendMessage(any());
    }

    @Test
    void registerWithUsernameTaken_ThenThrowUsernameTakenException() {
        RegisterRequest request = new RegisterRequest();
        doThrow(UsernameTakenException.class).when(registerValidator).validateRequest(any());

        Exception exception = assertThrows(UsernameTakenException.class,
                () -> authService.register(request));

        assertThat(exception).isInstanceOf(UsernameTakenException.class);
        verify(registerValidator).validateRequest(request);

        verify(userViewMapper, never()).mapRegisterRequestToUser(request);
        verify(jwtTokenService, never()).generateAccountActivationToken(any());
        verify(messageSender, never()).sendMessage(any());
    }

    @Test
    void registerWithInvalidPassword_ThenThrowInvalidPasswordException() {
        RegisterRequest request = new RegisterRequest();
        doThrow(InvalidPasswordException.class).when(registerValidator).validateRequest(any());

        Exception exception = assertThrows(InvalidPasswordException.class,
                () -> authService.register(request));

        assertThat(exception).isInstanceOf(InvalidPasswordException.class);
        verify(registerValidator).validateRequest(request);

        verify(userViewMapper, never()).mapRegisterRequestToUser(request);
        verify(jwtTokenService, never()).generateAccountActivationToken(any());
        verify(messageSender, never()).sendMessage(any());

    }

}