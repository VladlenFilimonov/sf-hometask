package com.sunfinance.hometask.verification.core.rules;

import com.sunfinance.hometask.api.UserInfo;
import com.sunfinance.hometask.api.error.NoPermissionsException;
import com.sunfinance.hometask.api.error.VerificationNotFoundException;
import com.sunfinance.hometask.api.event.verification.VerificationConfirmationEvent;
import com.sunfinance.hometask.verification.core.persistence.VerificationEntity;
import com.sunfinance.hometask.verification.core.persistence.VerificationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SameClientInfoVerificationConfirmationRuleTest {

    private final static UUID VERIFICATION_ID = UUID.fromString("8633e6f8-3b48-4130-af61-28a39bd58b34");
    private final static String CLIENT_IP = "127.0.0.1";
    private final static String CLIENT_USER_AGENT = "Test Agent";
    private final static String DIFFERENT_CLIENT_IP = "127.0.0.2";
    private final static String DIFFERENT_CLIENT_USER_AGENT = "Test Agent2";

    @Mock
    private VerificationRepository repository;
    @InjectMocks
    private SameClientInfoVerificationConfirmationRule victim;

    @Test
    public void shouldPassSameClientRuleSuccessfully() {
        var event = prepareEvent(CLIENT_IP, CLIENT_USER_AGENT);
        var persistedEntity = prepareEntity();
        when(repository.findByVerificationId(VERIFICATION_ID.toString()))
                .thenReturn(Optional.of(persistedEntity));
        victim.apply(event);
    }

    @Test(expected = NoPermissionsException.class)
    public void shouldThrowSameClientException_whenIpIsNotTheSame() {
        var event = prepareEvent(DIFFERENT_CLIENT_IP, CLIENT_USER_AGENT);
        var persistedEntity = prepareEntity();
        when(repository.findByVerificationId(VERIFICATION_ID.toString()))
                .thenReturn(Optional.of(persistedEntity));
        victim.apply(event);
    }

    @Test(expected = NoPermissionsException.class)
    public void shouldThrowSameClientException_whenAgentIsNotTheSame() {
        var event = prepareEvent(CLIENT_IP, DIFFERENT_CLIENT_USER_AGENT);
        var persistedEntity = prepareEntity();
        when(repository.findByVerificationId(VERIFICATION_ID.toString()))
                .thenReturn(Optional.of(persistedEntity));
        victim.apply(event);
    }

    @Test(expected = VerificationNotFoundException.class)
    public void shouldThrowNotFoundException_whenVerificationNotFound() {
        var verificationConfirmationEvent = prepareEvent(CLIENT_IP, CLIENT_USER_AGENT);
        when(repository.findByVerificationId(VERIFICATION_ID.toString()))
                .thenReturn(Optional.empty());
        victim.apply(verificationConfirmationEvent);
    }

    private VerificationEntity prepareEntity() {
        return VerificationEntity.builder()
                                 .id(1L)
                                 .verificationId(VERIFICATION_ID.toString())
                                 .clientIp(CLIENT_IP)
                                 .clientAgent(CLIENT_USER_AGENT)
                                 .build();
    }

    private VerificationConfirmationEvent prepareEvent(String clientIp, String userAgent) {
        return VerificationConfirmationEvent.builder()
                                            .verificationId(VERIFICATION_ID)
                                            .userInfo(UserInfo.builder()
                                                              .ipAddress(clientIp)
                                                              .userAgent(userAgent)
                                                              .build())
                                            .build();
    }
}