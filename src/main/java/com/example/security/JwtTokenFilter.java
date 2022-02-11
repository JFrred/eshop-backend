package com.example.security;

import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private static final String BEARER_HEADER = "Bearer ";
    private static final List<String> excludedUrls = Arrays.asList("/auth/perform_login",
            "/auth/perform_signup", "/auth/account-verification", "/products?name=**",
            "/products/**", "/categories");
    private final static List<AntPathRequestMatcher> excludedPathRequestMatcher = new ArrayList<>();

    static {
        excludedUrls.forEach(url -> excludedPathRequestMatcher.add(new AntPathRequestMatcher(url)));
    }

    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String token = getTokenFromRequest(request);

        if (jwtTokenService.isValid(token)) {
            UserDetails userDetails = userRepository
                    .findByUsername(jwtTokenService.getUsername(token))
                    .orElseThrow();

            UsernamePasswordAuthenticationToken authentication
                    = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource()
                    .buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return excludedPathRequestMatcher.stream().anyMatch(
                url -> url.matches(request));
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String header = Optional.of(request.getHeader(HttpHeaders.AUTHORIZATION))
                .orElseThrow(() -> new IllegalArgumentException("No Token Provided"));

        if (!header.startsWith(BEARER_HEADER))
            return "";

        return header.replace(BEARER_HEADER, "").trim();
    }

}