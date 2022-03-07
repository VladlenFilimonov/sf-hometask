package com.sunfinance.hometask.verification.core.service;

import com.sunfinance.hometask.api.event.verification.VerificationConfirmationEvent;
import com.sunfinance.hometask.api.event.verification.VerificationConfirmationResult;
import com.sunfinance.hometask.api.event.verification.VerificationCreateEvent;
import com.sunfinance.hometask.api.event.verification.VerificationCreateResult;

import java.util.Optional;

public interface VerificationService {

    Optional<VerificationCreateResult> create(VerificationCreateEvent event);

    Optional<VerificationConfirmationResult> confirm(VerificationConfirmationEvent event);

}
