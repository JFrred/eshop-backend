package com.example.model;

import com.example.model.abstracts.BaseEntity;
import com.example.model.embeddable.Address;
import com.example.model.enums.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

    @NotEmpty(message = "Username must not be empty")
    @Max(value = 20, message = "Username must be not longer than 20 characters")
    @Column(name = "username", unique = true)
    private String username;

    @NotEmpty(message = "Password must not be empty")
    @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters")
    @Column(name = "password")
    private String password;

    @NotEmpty(message = "First name must not be empty")
    @Column(name = "first_name")
    private String firstName;

    @NotEmpty(message = "Last name must not be empty")
    @Column(name = "last_name")
    private String lastName;

    @Email(message = "Invalid email address")
    @Column(name = "email_address", unique = true)
    private String email;

    @NotNull(message = "Account number is required")
    @Column(name = "acc_number", unique = true)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "user")
    private Cart cart;

    @Column(name = "is_enabled")
    private boolean enabled;

    @Column(name = "is_locked")
    private boolean locked;

    @Column(name = "is_credentials_non_expired")
    private boolean credentialsNonExpired;

    @Embedded
    private Address address;

    public User(String username, String password, String firstName, String lastName,
                String email, String accountNumber, Role role, Address address) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accountNumber = accountNumber;
        this.address = address;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" +  role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }
}
