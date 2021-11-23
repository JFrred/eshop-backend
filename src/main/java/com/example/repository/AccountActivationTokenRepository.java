package com.example.repository;

import com.example.model.AccountActivationToken;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountActivationTokenRepository extends JpaRepository<AccountActivationToken, Integer> {
    Optional<AccountActivationToken> findByToken(String token);
}
