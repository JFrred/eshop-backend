package com.example.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

import static com.example.config.MailConfig.PREFIX;

@Setter
@Configuration
@ConfigurationProperties(prefix = PREFIX)
class MailConfig {
    static final String PREFIX = "mail";

    private String host;
    private String username;
    private String password;
    private String transferProtocol;
    private int port;
    private boolean smtpAuth;
    private boolean smtpStarttlsEnable;
    private boolean debug;

    @Bean
    public JavaMailSender mailcatcherCfg() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", transferProtocol);
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", smtpStarttlsEnable);
        props.put("mail.debug", debug);

        return mailSender;
    }

}