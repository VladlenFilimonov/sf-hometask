package com.sunfinance.hometask.notification.out.template;

import com.sunfinance.hometask.api.event.verification.VerificationCreatedEvent;

import java.util.Optional;

public interface TemplateOutService {
    Optional<String> getBody(VerificationCreatedEvent event);
}
