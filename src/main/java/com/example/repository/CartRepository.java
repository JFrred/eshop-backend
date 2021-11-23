package com.example.repository;

import com.example.model.ShoppingSession;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingSessionRepository extends JpaRepository<ShoppingSession, Integer> {
    Optional<ShoppingSession> findByUser(User user);
}
