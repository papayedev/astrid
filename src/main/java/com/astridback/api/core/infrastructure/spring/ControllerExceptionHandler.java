package com.astridback.api.core.infrastructure.spring;

import com.astridback.api.domain.exceptions.UnauthorizedException;
import com.astridback.api.core.application.ports.APILogger;
import com.astridback.api.domain.exceptions.BadRequestException;
import com.astridback.api.domain.exceptions.ForbiddenException;
import com.astridback.api.domain.exceptions.NotFoundException;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private final APILogger apiLogger;

    public ControllerExceptionHandler(APILogger apiLogger) {
        this.apiLogger = apiLogger;
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            org.springframework.web.HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        apiLogger.warn("Method not supported: " + ex.getMessage());
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage(), null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        apiLogger.warn(e.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            org.springframework.web.HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        apiLogger.warn("Media type not supported: " + ex.getMessage());
        return buildResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage(), null);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
            org.springframework.web.HttpMediaTypeNotAcceptableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        apiLogger.warn("Media type not acceptable: " + ex.getMessage());
        return buildResponse(HttpStatus.NOT_ACCEPTABLE, ex.getMessage(), null);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
            org.springframework.web.bind.MissingPathVariableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        apiLogger.warn("Missing path variable: " + ex.getMessage());
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        apiLogger.warn("Missing request parameter: " + ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(
            MissingServletRequestPartException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        apiLogger.warn("Missing request part: " + ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(
            ServletRequestBindingException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        apiLogger.warn("Request binding error: " + ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        apiLogger.warn("Validation error: " + ex.getMessage());

        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (msg1, msg2) -> msg1));

        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed", errors);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        apiLogger.warn("Handler method validation error: " + ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed", null);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        apiLogger.warn("No handler found: " + ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(
            NoResourceFoundException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        apiLogger.warn("No resource found: " + ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(
            AsyncRequestTimeoutException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        apiLogger.warn("Async request timeout: " + ex.getMessage());
        return buildResponse(HttpStatus.SERVICE_UNAVAILABLE, "Request timed out", null);
    }

    @Override
    protected ResponseEntity<Object> handleErrorResponseException(
            org.springframework.web.ErrorResponseException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        apiLogger.warn("Error response exception: " + ex.getMessage());
        return buildResponse(HttpStatus.resolve(status.value()), ex.getMessage(), null);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(
            org.springframework.beans.ConversionNotSupportedException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        apiLogger.error("Conversion not supported: " + ex.getMessage());
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", null);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            org.springframework.beans.TypeMismatchException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        apiLogger.warn("Type mismatch: " + ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid parameter type: " + ex.getPropertyName(), null);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            org.springframework.http.converter.HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        apiLogger.warn("Message not readable: " + ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Malformed request body", null);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(
            org.springframework.http.converter.HttpMessageNotWritableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        apiLogger.error("Message not writable: " + ex.getMessage());
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", null);
    }

    @Override
    protected ResponseEntity<Object> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        apiLogger.warn("Upload too large: " + ex.getMessage());
        return buildResponse(HttpStatus.PAYLOAD_TOO_LARGE, "File size exceeds the allowed limit", null);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {
        apiLogger.warn("Constraint violation: " + ex.getMessage());

        Map<String, String> errors = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        v -> v.getMessage()));

        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed", errors);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
        apiLogger.warn(e.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException e) {
        apiLogger.warn(e.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException e) {
        apiLogger.warn(e.getMessage());
        return buildResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleForbiddenException(ForbiddenException e) {
        apiLogger.warn(e.getMessage());
        return buildResponse(HttpStatus.FORBIDDEN, e.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception e) {
        apiLogger.error("Unexpected error: " + e.getMessage());
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", null);
    }

    private ResponseEntity<Object> buildResponse(HttpStatus status, String message, Map<String, String> errors) {
        Map<String, Object> body = Map.of(
                "timestamp", Instant.now().toString(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message != null ? message : "",
                "errors", errors != null ? errors : Map.of());
        return ResponseEntity.status(status).body(body);
    }
}