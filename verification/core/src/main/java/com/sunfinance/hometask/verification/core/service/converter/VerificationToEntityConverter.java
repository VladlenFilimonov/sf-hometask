package com.sunfinance.hometask.verification.core.service.converter;

import com.sunfinance.hometask.verification.core.aggregate.Verification;
import com.sunfinance.hometask.verification.core.persistence.VerificationEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class VerificationToEntityConverter implements Converter<Verification, VerificationEntity> {

    @Override
    public VerificationEntity convert(Verification source) {
        return VerificationEntity.builder()
                                 .verificationId(source.getId().toString())
                                 .confirmed(source.isConfirmed())
                                 .clientIp(source.getUserInfo().getIp())
                                 .clientAgent(source.getUserInfo().getAgent())
                                 .subjectIdentity(source.getSubject().getIdentity())
                                 .subjectType(source.getSubject().getType().name())
                                 .code(source.getCode())
                                 .build();
    }
}
