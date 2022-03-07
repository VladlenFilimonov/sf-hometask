package com.sunfinance.hometask.verification.core.service.converter;

import com.sunfinance.hometask.api.SubjectIdentity;
import com.sunfinance.hometask.api.VerificationSubject;
import com.sunfinance.hometask.api.event.verification.VerificationCreatedEvent;
import com.sunfinance.hometask.verification.core.aggregate.Verification;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class VerificationToCreatedEventConverter implements Converter<Verification, VerificationCreatedEvent> {

    @Override
    public VerificationCreatedEvent convert(Verification source) {
        var type = source.getSubject().getType();
        var identity = SubjectIdentity.build(type, source.getSubject().getIdentity());
        return VerificationCreatedEvent.builder()
                                       .code(source.getCode())
                                       .subject(VerificationSubject.builder()
                                                                   .identity(identity)
                                                                   .type(type)
                                                                   .build())
                                       .id(source.getId())
                                       .build();
    }
}
