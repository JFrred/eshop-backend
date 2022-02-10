package com.example.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor(onConstructor_ = @JsonCreator(mode = JsonCreator.Mode.PROPERTIES))
public class LoginRequest {

    @NotNull(message = "Username must not be null")
    @NotEmpty(message = "Username must not be empty")
    private final String username;

    @NotNull(message = "Password must not be null")
    @Size(min = 6, max = 30, message = "Password length must be between 6 and 30")
    private final char[] password;
}
