package com.sunfinance.hometask.verification.core.service.converter;

import com.sunfinance.hometask.api.SubjectType;
import com.sunfinance.hometask.verification.core.aggregate.Subject;
import com.sunfinance.hometask.verification.core.aggregate.UserInfo;
import com.sunfinance.hometask.verification.core.aggregate.Verification;
import com.sunfinance.hometask.verification.core.persistence.VerificationEntity;
import org.junit.Test;

import java.math.BigInteger;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class VerificationToEntityConverterTest {

    private final static UUID VERIFICATION_ID = UUID.fromString("8633e6f8-3b48-4130-af61-28a39bd58b34");
    private final static BigInteger CODE = BigInteger.valueOf(1234L);
    private static final String IP_ADDRESS = "127.0.0.1";
    private static final String USER_AGENT = "test-agent";
    private static final SubjectType SUBJECT_TYPE = SubjectType.EMAIL_CONFIRMATION;
    private static final String EMAIL_IDENTITY = "john.doe@abc.xyz";

    @Test
    public void shouldConvertSuccessfully() {
        var verification = prepareVerification();
        var expectedEntity = prepareEntity();

        var converter = new VerificationToEntityConverter();
        var result = converter.convert(verification);
        assertEquals(expectedEntity, result);
    }

    private VerificationEntity prepareEntity() {
        return VerificationEntity.builder()
                                 .verificationId(VERIFICATION_ID.toString())
                                 .subjectType(SUBJECT_TYPE.name())
                                 .subjectIdentity(EMAIL_IDENTITY)
                                 .clientIp(IP_ADDRESS)
                                 .clientAgent(USER_AGENT)
                                 .code(CODE)
                                 .confirmed(true)
                                 .build();
    }

    private Verification prepareVerification() {
        return Verification.builder()
                           .id(VERIFICATION_ID)
                           .code(CODE)
                           .confirmed(true)
                           .userInfo(UserInfo.builder()
                                             .ip(IP_ADDRESS)
                                             .agent(USER_AGENT)
                                             .build())
                           .subject(Subject.builder()
                                           .type(SUBJECT_TYPE)
                                           .identity(EMAIL_IDENTITY)
                                           .build())
                           .build();
    }

}