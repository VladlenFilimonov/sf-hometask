package com.sunfinance.hometask.verification.core.cache;

import com.sunfinance.hometask.api.EmailIdentity;
import com.sunfinance.hometask.api.SubjectType;
import com.sunfinance.hometask.api.UserInfo;
import com.sunfinance.hometask.api.VerificationSubject;
import com.sunfinance.hometask.api.event.verification.VerificationCreateEvent;
import io.lettuce.core.RedisException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RedisCacheImplTest {

    private static final String IP_ADDRESS = "127.0.0.1";
    private static final String USER_AGENT = "test-agent";
    private static final SubjectType SUBJECT_TYPE = SubjectType.EMAIL_CONFIRMATION;
    private static final String EMAIL_IDENTITY = "john.doe@abc.xyz";

    @Mock
    private ValueOperations<String, VerificationCreateEvent> verificationCache;
    @InjectMocks
    private RedisCacheImpl victim;

    @Test(expected = RedisException.class)
    public void shouldThrowThrowException_whenRedisTransactionEnabled() {
        var verificationCreateEvent = prepareEvent();
        var expectedKey = "verification:create:" + verificationCreateEvent.getSubject().hashCode();
        when(verificationCache.setIfAbsent(expectedKey, verificationCreateEvent, Duration.ofMinutes(5L)))
                .thenReturn(null);
        victim.saveIfAbsent(expectedKey, verificationCreateEvent, Duration.ofMinutes(5L));
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