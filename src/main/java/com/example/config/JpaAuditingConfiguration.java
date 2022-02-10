package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Instant;
import java.util.Optional;

import static com.example.config.JpaAuditingConfiguration.DATE_TIME_PROVIDER;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = DATE_TIME_PROVIDER)
class JpaAuditingConfiguration {
    static final String DATE_TIME_PROVIDER = "auditingDateTimeProvider";

    @Bean(name = "auditingDateTimeProvider")
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(Instant.now());
    }

}