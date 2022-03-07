package com.sunfinance.hometask.verification.rest.controller;

import com.sunfinance.hometask.api.UserInfo;
import com.sunfinance.hometask.api.event.Topic;
import com.sunfinance.hometask.api.event.verification.VerificationCreateEvent;
import com.sunfinance.hometask.api.event.verification.VerificationCreateResult;
import com.sunfinance.hometask.api.rest.verification.VerificationCreateRequest;
import com.sunfinance.hometask.api.rest.verification.VerificationCreateResponse;
import com.sunfinance.hometask.event.EventService;
import com.sunfinance.hometask.verification.rest.converter.VerificationCreateRequestToEventConverter;
import com.sunfinance.hometask.verification.rest.converter.VerificationCreateResultToResponseConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
public class VerificationCreateController {

    private final EventService eventService;
    private final VerificationCreateRequestToEventConverter toEventConverter;
    private final VerificationCreateResultToResponseConverter toResponseConverter;

    @PostMapping("/verifications")
    public CompletableFuture<VerificationCreateResponse> create(@Valid @NotNull @RequestBody VerificationCreateRequest createRequest,
                                                                HttpServletRequest httpServletRequest) {
        return CompletableFuture.supplyAsync(() -> extractUserInfo(httpServletRequest))
                                .thenApply(userInfo -> toEventConverter.convert(createRequest, userInfo))
                                .thenCompose(this::sendEvent)
                                .thenApply(toResponseConverter::convert);
    }

    private CompletionStage<VerificationCreateResult> sendEvent(VerificationCreateEvent event) {
        return eventService.sendAndReceive(
                event,
                Topic.VERIFICATION_CREATE_REQUEST,
                Topic.VERIFICATION_CREATE_REPLY,
                VerificationCreateResult.class);
    }

    private UserInfo extractUserInfo(HttpServletRequest httpServletRequest) {
        return UserInfo.builder()
                       .userAgent(httpServletRequest.getHeader(HttpHeaders.USER_AGENT))
                       .ipAddress(httpServletRequest.getRemoteAddr())
                       .build();
    }
}
