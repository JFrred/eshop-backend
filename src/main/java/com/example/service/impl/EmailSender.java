package com.example.service.impl;

import com.example.model.abstracts.Message;
import com.example.service.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSender implements MessageSender {
    private final JavaMailSender mailSender;

    @Value("${server.port}")
    private int serverPort;
    @Value("${email.sender}")
    private String from;
    @Value("${email.subject}")
    private String subject;
    @Value("${email.text}")
    private String text;

    @Override
    public void sendMessage(Message message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(message.getTo());
        simpleMailMessage.setSubject(subject);
        String activationUrl = generatedActivationUrl(message.getToken());
        simpleMailMessage.setText(activationUrl);

        mailSender.send(simpleMailMessage);
    }

    private String generatedActivationUrl(String token) {
        return String.format(text, serverPort, token);
    }

    static class ActivationEmail extends Message {
        public ActivationEmail(String recipientAddressEmail, String activationToken) {
            super(recipientAddressEmail, activationToken);
        }
    }
}
