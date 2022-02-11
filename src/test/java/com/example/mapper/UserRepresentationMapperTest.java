package com.example.mapper;

import com.example.dto.RegisterRequest;
import com.example.dto.UserRepresentation;
import com.example.model.User;
import com.example.model.embeddable.Address;
import com.example.model.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepresentationMapperTest {

    @Autowired
    private UserViewMapperImpl mapper;

    @Test
    void testMapRegisterRequestToUser() {
        RegisterRequest registerRequest = new RegisterRequest();
        String rawPassword = "password";
        registerRequest.setPassword(rawPassword.toCharArray());

        User actual = mapper.mapRegisterRequestToUser(registerRequest);

        String encodedPassword = actual.getPassword();
        assertThat(encodedPassword).isNotEqualTo(rawPassword);
    }

    @Test
    void testMapToView() {
        User user = new User("bob_ross", "password", "Bob", "Ross", "bob@ross.test",
                "1234-1234-1234-1234", Role.USER, new Address());

        UserRepresentation actual = mapper.mapToView(user);

        assertThat(actual.getUsername()).isEqualTo("bob_ross");
        assertThat(actual.getFullName()).isEqualTo("Bob Ross");
    }
}