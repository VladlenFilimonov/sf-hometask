package com.sunfinance.hometask.verification.rest.error;

import com.sunfinance.hometask.api.error.DuplicatedVerificationException;
import com.sunfinance.hometask.api.error.ErrorCode;
import com.sunfinance.hometask.api.error.InvalidCodeException;
import com.sunfinance.hometask.api.error.InvalidSubjectException;
import com.sunfinance.hometask.api.error.NoPermissionsException;
import com.sunfinance.hometask.api.error.VerificationConfirmedAlreadyException;
import com.sunfinance.hometask.api.error.VerificationExpiredException;
import com.sunfinance.hometask.api.error.VerificationMaxAttemptsException;
import com.sunfinance.hometask.api.error.VerificationNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ValidationException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class RestVerificationExceptionHandler {

    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage validationException(BindException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        var message = extractValidationErrors(ex);
        return ErrorMessage.builder()
                           .code(ErrorCode.VALIDATION_EXCEPTION.name())
                           .description(ErrorCode.VALIDATION_EXCEPTION.description())
                           .message(message)
                           .build();
    }

    @ExceptionHandler(value = ValidationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage validationException(ValidationException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return ErrorMessage.builder()
                           .code(ErrorCode.VALIDATION_EXCEPTION.name())
                           .description(ErrorCode.VALIDATION_EXCEPTION.description())
                           .message(ex.getMessage())
                           .build();
    }

    @ExceptionHandler(value = HttpMessageConversionException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage jsonParsingException(HttpMessageConversionException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return ErrorMessage.builder()
                           .code(ErrorCode.MALFORMED_JSON_PASSED.name())
                           .description(ErrorCode.MALFORMED_JSON_PASSED.description())
                           .message(ex.getMessage())
                           .build();
    }

    @ExceptionHandler(value = DuplicatedVerificationException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage duplicatedVerificationException(DuplicatedVerificationException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return ErrorMessage.builder()
                           .code(ErrorCode.DUPLICATED_VERIFICATION.name())
                           .description(ErrorCode.DUPLICATED_VERIFICATION.description())
                           .message(ex.getMessage())
                           .build();
    }

    @ExceptionHandler(value = InvalidSubjectException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorMessage invalidSubjectException(InvalidSubjectException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return ErrorMessage.builder()
                           .code(ErrorCode.INVALID_SUBJECT.name())
                           .description(ErrorCode.INVALID_SUBJECT.description())
                           .message(ex.getMessage())
                           .build();
    }

    @ExceptionHandler(value = NoPermissionsException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorMessage noPermissionsException(NoPermissionsException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return ErrorMessage.builder()
                           .code(ErrorCode.NO_PERMISSIONS.name())
                           .description(ErrorCode.NO_PERMISSIONS.description())
                           .message(ex.getMessage())
                           .build();
    }

    @ExceptionHandler(value = VerificationNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage verificationNotFoundException(VerificationNotFoundException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return ErrorMessage.builder()
                           .code(ErrorCode.VERIFICATION_NOT_FOUND.name())
                           .description(ErrorCode.VERIFICATION_NOT_FOUND.description())
                           .message(ex.getMessage())
                           .build();
    }

    @ExceptionHandler(value = VerificationExpiredException.class)
    @ResponseStatus(value = HttpStatus.GONE)
    public ErrorMessage verificationExpiredException(VerificationExpiredException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return ErrorMessage.builder()
                           .code(ErrorCode.VERIFICATION_EXPIRED.name())
                           .description(ErrorCode.VERIFICATION_EXPIRED.description())
                           .message(ex.getMessage())
                           .build();
    }

    @ExceptionHandler(value = InvalidCodeException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorMessage invalidCodeException(InvalidCodeException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return ErrorMessage.builder()
                           .code(ErrorCode.INVALID_VERIFICATION_CODE.name())
                           .description(ErrorCode.INVALID_VERIFICATION_CODE.description())
                           .message(ex.getMessage())
                           .build();
    }

    @ExceptionHandler(value = VerificationConfirmedAlreadyException.class)
    @ResponseStatus(value = HttpStatus.ALREADY_REPORTED)
    public ResponseEntity<?> verificationConfirmedAlreadyException(VerificationConfirmedAlreadyException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED.value())
                             .build();
    }

    @ExceptionHandler(value = VerificationMaxAttemptsException.class)
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public ErrorMessage verificationMaxAttemptsException(VerificationMaxAttemptsException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return ErrorMessage.builder()
                           .code(ErrorCode.VALIDATION_EXCEPTION.name())
                           .description(ErrorCode.VALIDATION_EXCEPTION.description())
                           .message(ex.getMessage())
                           .build();
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage internalError(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return ErrorMessage.builder()
                           .code(ErrorCode.INTERNAL_ERROR.name())
                           .description(ErrorCode.INTERNAL_ERROR.description())
                           .message(ex.getMessage())
                           .build();
    }

    private String extractValidationErrors(BindException ex) {
        return ex.getAllErrors()
                 .stream()
                 .map(DefaultMessageSourceResolvable::getDefaultMessage)
                 .collect(Collectors.joining("; \n"));
    }

}
