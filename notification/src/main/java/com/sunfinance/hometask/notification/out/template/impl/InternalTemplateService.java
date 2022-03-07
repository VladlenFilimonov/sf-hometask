package com.sunfinance.hometask.notification.out.template.impl;

import com.sunfinance.hometask.api.event.verification.VerificationCreatedEvent;
import com.sunfinance.hometask.api.rest.template.TemplateGetBodyOutRequest;
import com.sunfinance.hometask.notification.out.template.TemplateClient;
import com.sunfinance.hometask.notification.out.template.TemplateOutService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InternalTemplateService implements TemplateOutService {

    private final TemplateClient templateClient;
    private final ConversionService conversionService;

    @Override
    public Optional<String> getBody(VerificationCreatedEvent event) {
        return Optional.of(buildRequest(event))
                       .map(templateClient::getBody);
    }

    private TemplateGetBodyOutRequest buildRequest(VerificationCreatedEvent event) {
        return TemplateGetBodyOutRequest.builder()
                                        .slug(conversionService.convert(event.getSubject().getType(), String.class))
                                        .variables(Map.of("code", event.getCode().toString()))
                                        .build();
    }
}
