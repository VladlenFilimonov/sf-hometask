package com.sunfinance.hometask.verification.core.service.impl;

import com.sunfinance.hometask.api.event.verification.VerificationConfirmationEvent;
import com.sunfinance.hometask.api.event.verification.VerificationCreateEvent;
import com.sunfinance.hometask.verification.core.rules.BusinessRule;
import com.sunfinance.hometask.verification.core.rules.VerificationConfirmationBusinessRules;
import com.sunfinance.hometask.verification.core.rules.VerificationCreationBusinessRules;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VerificationFacadeTest {

    private final VerificationCreateEvent createEvent = VerificationCreateEvent.builder()
                                                                               .build();

    private final VerificationConfirmationEvent confirmationEvent = VerificationConfirmationEvent.builder()
                                                                                                 .build();

    @Mock
    private VerificationCreationBusinessRules creationBusinessRules;
    @Mock
    private VerificationConfirmationBusinessRules confirmationBusinessRules;
    @Mock
    private VerificationCreationService verificationCreationService;
    @Mock
    private VerificationConfirmationService verificationConfirmationService;
    @Mock
    private BusinessRule<VerificationCreateEvent> createEventBusinessRule;
    @Mock
    private BusinessRule<VerificationConfirmationEvent> confirmationEventBusinessRule;

    @InjectMocks
    private VerificationFacade victim;

    @Test
    public void shouldApplyRule_andCallVerificationCreationService() {
        var businessRules = List.of(createEventBusinessRule);
        when(creationBusinessRules.getRules()).thenReturn(businessRules);
        victim.create(createEvent);

        verify(createEventBusinessRule, times(1)).apply(createEvent);
        verify(verificationCreationService, times(1)).create(createEvent);
    }

    @Test
    public void shouldApplyRule_andCallVerificationConfirmationService() {
        var businessRules = List.of(confirmationEventBusinessRule);
        when(confirmationBusinessRules.getRules()).thenReturn(businessRules);
        victim.confirm(confirmationEvent);

        verify(confirmationEventBusinessRule, times(1)).apply(confirmationEvent);
        verify(verificationConfirmationService, times(1)).confirm(confirmationEvent);
    }
}