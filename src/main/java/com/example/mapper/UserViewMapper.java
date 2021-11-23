package com.example.mapper;

import com.example.dto.RegisterRequest;
import com.example.dto.UserRepresentation;
import com.example.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserViewMapper {
    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Mapping(target = "password",
            expression = "java(passwordEncoder.encode(fromCharArray(registerRequest.getPassword())))")
    @Mapping(target = "address.city", source = "city")
    @Mapping(target = "address.street", source = "street")
    @Mapping(target = "address.postalCode", source = "postalCode")
    public abstract User mapRegisterRequestToUser(RegisterRequest registerRequest);

    public abstract UserRepresentation mapToView(User user);

    public String fromCharArray(char[] password) {
        return String.valueOf(password);
    }
}