package com.example.validation;

import com.example.dto.RegisterRequest;
import com.example.exception.InvalidAddressEmailException;
import com.example.exception.UsernameTakenException;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterValidator {
    private final UserRepository userRepository;
    private final PasswordValidator passwordValidator;

    public void validateRequest(RegisterRequest registerRequest) {
        if (isUsernameTaken(registerRequest.getUsername()))
            throw new UsernameTakenException();

        passwordValidator.validate(registerRequest.getPassword(),
                registerRequest.getConfirmPassword());

        if (isAddressEmailTaken(registerRequest.getEmail()))
            throw new InvalidAddressEmailException();
    }

    private boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    private boolean isAddressEmailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
