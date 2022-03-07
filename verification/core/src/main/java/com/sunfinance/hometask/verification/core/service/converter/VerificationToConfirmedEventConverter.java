package com.sunfinance.hometask.verification.core.service.converter;

import com.sunfinance.hometask.api.SubjectIdentity;
import com.sunfinance.hometask.api.VerificationSubject;
import com.sunfinance.hometask.api.event.verification.VerificationConfirmedEvent;
import com.sunfinance.hometask.verification.core.aggregate.Verification;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class VerificationToConfirmedEventConverter implements Converter<Verification, VerificationConfirmedEvent> {
    @Override
    public VerificationConfirmedEvent convert(Verification source) {
        var type = source.getSubject().getType();
        var identity = SubjectIdentity.build(type, source.getSubject().getIdentity());
        return VerificationConfirmedEvent.builder()
                                         .id(source.getId())
                                         .subject(VerificationSubject.builder()
                                                                     .type(type)
                                                                     .identity(identity)
                                                                     .build())
                                         .build();
    }
}
