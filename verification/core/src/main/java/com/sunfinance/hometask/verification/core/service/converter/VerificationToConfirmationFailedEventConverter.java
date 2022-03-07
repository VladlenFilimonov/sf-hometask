package com.sunfinance.hometask.verification.core.service.converter;

import com.sunfinance.hometask.api.SubjectIdentity;
import com.sunfinance.hometask.api.VerificationSubject;
import com.sunfinance.hometask.api.event.verification.VerificationConfirmationFailedEvent;
import com.sunfinance.hometask.verification.core.aggregate.Verification;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class VerificationToConfirmationFailedEventConverter implements Converter<Verification, VerificationConfirmationFailedEvent> {

    @Override
    public VerificationConfirmationFailedEvent convert(Verification source) {
        var type = source.getSubject().getType();
        var identity = SubjectIdentity.build(type, source.getSubject().getIdentity());
        return VerificationConfirmationFailedEvent.builder()
                                                  .id(source.getId())
                                                  .subject(VerificationSubject.builder()
                                                                              .identity(identity)
                                                                              .type(type)
                                                                              .build())
                                                  .build();
    }
}
