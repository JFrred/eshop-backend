package com.example.security;

import com.example.exception.InvalidTokenException;
import com.example.model.AccountActivationToken;
import com.example.model.User;
import com.example.model.enums.Role;
import com.example.repository.AccountActivationTokenRepository;
import com.example.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {
    private final AccountActivationTokenRepository authTokenRepository;
    private final UserRepository userRepository;

    @Value("${activation.token.expiration}")
    private int activationTokenExpiration;
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.issuer}")
    private String jwtIssuer;
    @Value("${token.expiration}")
    private int tokenExpiration;

    public String generateAccountActivationToken(User user) {
        String token = UUID.randomUUID().toString();
        AccountActivationToken accountActivationToken = new AccountActivationToken(
                user, token, Instant.now().plus(activationTokenExpiration, ChronoUnit.MILLIS));
        authTokenRepository.save(accountActivationToken);

        return token;
    }

    public String generateAccessToken(UserDetails user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .addClaims(Map.of("admin", user.getAuthorities().contains(Role.ADMIN)))
                .setIssuer(jwtIssuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(Instant.now()
                        .plus(tokenExpiration, ChronoUnit.MILLIS).toEpochMilli()))
                .signWith(getSigningKey())
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Transactional
    public void enableUser(String token) {
        AccountActivationToken authToken = authTokenRepository.findByToken(token)
                .orElseThrow(InvalidTokenException::new);
        tokenExpired(authToken);

        User user = authToken.getUser();
        if (user == null)
            throw new UsernameNotFoundException("Could not find user");

        user.setEnabled(true);
        userRepository.save(user);

        authTokenRepository.delete(authToken);
    }

    private void tokenExpired(AccountActivationToken authToken) {
        long expirationDate = authToken.getCreatedAt().toEpochMilli() + tokenExpiration;
        long currentDate = Instant.now().toEpochMilli();

        if (expirationDate - currentDate < 0) {
            authTokenRepository.delete(authToken);
            userRepository.delete(authToken.getUser());

            throw new InvalidTokenException("Token expired");
        }
    }

    public boolean isValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token - {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token - {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token - {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty - {}", ex.getMessage());
        }
        return false;
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

}