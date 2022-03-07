package com.sunfinance.hometask.verification.core.rules;

import com.sunfinance.hometask.api.EmailIdentity;
import com.sunfinance.hometask.api.SubjectType;
import com.sunfinance.hometask.api.UserInfo;
import com.sunfinance.hometask.api.VerificationSubject;
import com.sunfinance.hometask.api.error.DuplicatedVerificationException;
import com.sunfinance.hometask.api.event.verification.VerificationCreateEvent;
import com.sunfinance.hometask.verification.core.cache.CacheService;
import com.sunfinance.hometask.verification.core.config.properties.VerificationProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DuplicateVerificationCreationRuleTest {

    private static final String IP_ADDRESS = "127.0.0.1";
    private static final String USER_AGENT = "test-agent";
    private static final SubjectType SUBJECT_TYPE = SubjectType.EMAIL_CONFIRMATION;
    private static final String EMAIL_IDENTITY = "john.doe@abc.xyz";

    @Mock
    private VerificationProperties verificationProperties;
    @Mock
    private CacheService cacheService;

    @InjectMocks
    private DuplicateVerificationCreationRule victim;

    @Test
    public void shouldSuccessfullyPersistVerificationCreationEvent() {
        var verificationCreateEvent = prepareEvent();
        var expectedKey = "verification:create:" + verificationCreateEvent.getSubject().hashCode();
        when(verificationProperties.getTtl())
                .thenReturn(Duration.ofMinutes(5L));
        when(cacheService.saveIfAbsent(expectedKey, verificationCreateEvent, Duration.ofMinutes(5L)))
                .thenReturn(Boolean.TRUE);
        victim.apply(verificationCreateEvent);
    }

    @Test(expected = DuplicatedVerificationException.class)
    public void shouldThrowDuplicationException_whenEventPersistedAlready() {
        var verificationCreateEvent = prepareEvent();
        var expectedKey = "verification:create:" + verificationCreateEvent.getSubject().hashCode();
        when(verificationProperties.getTtl())
                .thenReturn(Duration.ofMinutes(5L));
        when(cacheService.saveIfAbsent(expectedKey, verificationCreateEvent, Duration.ofMinutes(5L)))
                .thenReturn(Boolean.FALSE);
        victim.apply(verificationCreateEvent);
    }

    private VerificationCreateEvent prepareEvent() {
        return VerificationCreateEvent.builder()
                                      .userInfo(UserInfo.builder()
                                                        .ipAddress(IP_ADDRESS)
                                                        .userAgent(USER_AGENT)
                                                        .build())
                                      .subject(VerificationSubject.builder()
                                                                  .type(SUBJECT_TYPE)
                                                                  .identity(EmailIdentity.builder()
                                                                                         .val(EMAIL_IDENTITY)
                                                                                         .build())
                                                                  .build())
                                      .build();
    }

}