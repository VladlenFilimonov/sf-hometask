package com.sunfinance.hometask.verification.core.aggregate;

import com.sunfinance.hometask.api.SubjectType;
import com.sunfinance.hometask.api.event.verification.VerificationCreateEvent;
import com.sunfinance.hometask.verification.core.persistence.VerificationEntity;
import com.sunfinance.hometask.verification.core.service.generator.CodeGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.IdGenerator;

import java.math.BigInteger;
import java.util.UUID;

@Service
@AllArgsConstructor
public class VerificationFactory {

    private final IdGenerator idGenerator;
    private final CodeGenerator<BigInteger> codeGenerator;

    public Verification create(VerificationCreateEvent event) {
        return buildVerification(event);
    }

    private Verification buildVerification(VerificationCreateEvent event) {
        return Verification.builder()
                           .id(idGenerator.generateId())
                           .confirmed(false)
                           .code(codeGenerator.generateCode())
                           .subject(Subject.builder()
                                           .identity(event.getSubject().getIdentity().getVal())
                                           .type(event.getSubject().getType())
                                           .build())
                           .userInfo(UserInfo.builder()
                                             .ip(event.getUserInfo().getIpAddress())
                                             .agent(event.getUserInfo().getUserAgent())
                                             .build())
                           .build();
    }

    public Verification create(VerificationEntity entity) {
        return Verification.builder()
                           .id(UUID.fromString(entity.getVerificationId()))
                           .confirmed(entity.isConfirmed())
                           .code(entity.getCode())
                           .subject(Subject.builder()
                                           .identity(entity.getSubjectIdentity())
                                           .type(SubjectType.valueOf(entity.getSubjectType()))
                                           .build())
                           .userInfo(UserInfo.builder()
                                             .ip(entity.getClientIp())
                                             .agent(entity.getClientAgent())
                                             .build())
                           .build();
    }

    public Verification confirm(Verification verification) {
        return verification.toBuilder()
                           .confirmed(true)
                           .build();
    }

}
