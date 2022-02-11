package com.example.model.enums;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {
    USER("READ_PRIVILEGE"),
    ADMIN("WRITE_PRIVILEGE");

    private final String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}


