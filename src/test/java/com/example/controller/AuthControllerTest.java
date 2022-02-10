package com.example.controller;

import com.example.dto.AuthenticationResponse;
import com.example.dto.LoginRequest;
import com.example.dto.RegisterRequest;
import com.example.exception.InvalidAddressEmailException;
import com.example.exception.InvalidPasswordException;
import com.example.exception.InvalidTokenException;
import com.example.exception.UsernameTakenException;
import com.example.security.JwtTokenService;
import com.example.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    private final String LOGIN_PATH = "/auth/perform_login";
    private final String REGISTER_PATH = "/auth/perform_signup";
    private final String TOKEN_PATH = "/auth/account-verification";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;
    @MockBean
    private JwtTokenService jwtTokenService;

    LoginRequest request;

    @BeforeEach
    void setup() {
        request = new LoginRequest("username", "password1234".toCharArray());
    }

    @Test
    void login_WithValidCredentials_ShouldSucceedWith200() throws Exception {
        AuthenticationResponse authResponse = new AuthenticationResponse("test_token", null);

        given(authService.login(any())).willReturn(authResponse);

        mockMvc.perform(post(LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, "test_token"))
                .andExpect(status().isOk());
    }

    @Test
    void register_ShouldSucceedWith201() throws Exception {
        mockMvc.perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RegisterRequest())))
                .andExpect(status().isCreated());
    }

    @Test
    void register_UsernameTaken_ShouldFailWith409() throws Exception {
        doThrow(new UsernameTakenException()).when(authService).register(any());

        mockMvc.perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RegisterRequest())))
                .andExpect(status().isConflict());
    }

    @Test
    void register_InvalidPassword_ShouldFailWith422() throws Exception {
        doThrow(new InvalidPasswordException()).when(authService).register(any());

        mockMvc.perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RegisterRequest())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void register_InvalidAddressEmail_ShouldFailWith422() throws Exception {
        doThrow(new InvalidAddressEmailException()).when(authService).register(any());

        mockMvc.perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RegisterRequest())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void authenticateUser_ShouldSucceedWith201() throws Exception {
        mockMvc.perform(post(TOKEN_PATH)
                        .param("token", "auth_token"))
                .andExpect(status().isCreated());
    }

    @Test
    void authenticateUser_ShouldFailWith401() throws Exception {
        doThrow(InvalidTokenException.class).when(jwtTokenService).enableUser(anyString());

        mockMvc.perform(post(TOKEN_PATH)
                        .param("token", "auth_token"))
                .andExpect(status().isUnauthorized());
    }

}