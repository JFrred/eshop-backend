package com.example.service.impl;

import com.example.dto.AuthenticationResponse;
import com.example.dto.LoginRequest;
import com.example.dto.RegisterRequest;
import com.example.mapper.UserViewMapper;
import com.example.model.Cart;
import com.example.model.User;
import com.example.repository.CartRepository;
import com.example.repository.UserRepository;
import com.example.security.JwtTokenService;
import com.example.service.AuthService;
import com.example.service.MessageSender;
import com.example.service.impl.EmailSender.ActivationEmail;
import com.example.validation.RegisterValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.CharBuffer;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final MessageSender messageSender;
    private final RegisterValidator registerValidator;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserViewMapper userViewMapper;

    @Override
    @Transactional
    public String register(RegisterRequest registerRequest) {
        registerValidator.validateRequest(registerRequest);
        User user = userRepository.save(userViewMapper.mapRegisterRequestToUser(registerRequest));

        cartRepository.save(new Cart(user));

        String token = jwtTokenService.generateAccountActivationToken(user);
        messageSender.sendMessage(new ActivationEmail(user.getEmail(), token));

        return token;
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

        String username = userViewMapper.mapToView(principal).getUsername();
        return new AuthenticationResponse(token, username);
    }

    @Override
    public User getCurrentUser() {
        final UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Couldn't find user " + principal.getUsername()));
    }

}