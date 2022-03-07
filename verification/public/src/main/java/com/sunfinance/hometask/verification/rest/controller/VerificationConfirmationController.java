package com.sunfinance.hometask.verification.rest.controller;

import com.sunfinance.hometask.api.UserInfo;
import com.sunfinance.hometask.api.event.Topic;
import com.sunfinance.hometask.api.event.verification.VerificationConfirmationEvent;
import com.sunfinance.hometask.api.event.verification.VerificationConfirmationResult;
import com.sunfinance.hometask.api.rest.verification.VerificationConfirmationRequest;
import com.sunfinance.hometask.event.EventService;
import com.sunfinance.hometask.verification.rest.converter.VerificationConfirmationRequestToEventConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
public class VerificationConfirmationController {

    private final EventService eventService;
    private final VerificationConfirmationRequestToEventConverter toEventConverter;

    @PostMapping("/verifications/{id}/confirm")
    public CompletableFuture<ResponseEntity<?>> confirm(@PathVariable UUID id,
                                                        @Valid @NotNull @RequestBody VerificationConfirmationRequest createRequest,
                                                        HttpServletRequest httpServletRequest) {
        return CompletableFuture.supplyAsync(() -> extractUserInfo(httpServletRequest))
                                .thenApply(userInfo -> toEventConverter.convert(createRequest, userInfo, id))
                                .thenCompose(this::sendEvent)
                                .thenApply(this::toResponse);
    }

    private CompletionStage<VerificationConfirmationResult> sendEvent(VerificationConfirmationEvent event) {
        return eventService.sendAndReceive(
                event,
                Topic.VERIFICATION_CONFIRMATION_REQUEST,
                Topic.VERIFICATION_CONFIRMATION_REPLY,
                VerificationConfirmationResult.class);
    }

    private ResponseEntity<?> toResponse(VerificationConfirmationResult verificationConfirmationResult) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }

    private UserInfo extractUserInfo(HttpServletRequest httpServletRequest) {
        return UserInfo.builder()
                       .userAgent(httpServletRequest.getHeader(HttpHeaders.USER_AGENT))
                       .ipAddress(httpServletRequest.getRemoteAddr())
                       .build();
    }
}
