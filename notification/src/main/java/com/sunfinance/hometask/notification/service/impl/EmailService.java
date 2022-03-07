package com.sunfinance.hometask.notification.service.impl;

import com.sunfinance.hometask.notification.aggregate.Notification;
import com.sunfinance.hometask.notification.aggregate.NotificationFactory;
import com.sunfinance.hometask.notification.service.NotificationStrategy;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.sunfinance.hometask.api.SubjectTypeConstant.EMAIL_CONFIRMATION_NAME;

@Service(EMAIL_CONFIRMATION_NAME)
@AllArgsConstructor
public class EmailService implements NotificationStrategy {

    private final JavaMailSender javaMailSender;
    private final NotificationFactory notificationFactory;

    @Override
    @Retryable
    public Optional<Notification> send(Notification notification) {
        return Optional.of(buildMessage(notification))
                       .map(mail -> {
                           javaMailSender.send(mail);
                           return notification;
                       })
                       .map(notificationFactory::dispatch);
    }

    private SimpleMailMessage buildMessage(Notification notification) {
        var mail = new SimpleMailMessage();
        mail.setFrom("notification@sunfinance.com");
        mail.setTo(notification.getRecipient());
        mail.setSubject("Verification code");
        mail.setText(notification.getBody());
        return mail;
    }
}
