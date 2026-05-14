package dev.charlles.teachgram.shared.error;

import dev.charlles.teachgram.shared.web.TraceIdFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    ResponseEntity<ProblemDetail> handleApiException(ApiException exception, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(exception.getStatus(), exception.getMessage());
        problem.setTitle(titleFor(exception.getStatus()));
        problem.setType(URI.create("https://teachgram.pro/problems/" + exception.getCode()));
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("traceId", TraceIdFilter.currentTraceId());
        return ResponseEntity.status(exception.getStatus()).body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "One or more fields are invalid.");
        problem.setTitle("Validation error");
        problem.setType(URI.create("https://teachgram.pro/problems/validation-error"));
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("traceId", TraceIdFilter.currentTraceId());
        Map<String, String[]> errors = new LinkedHashMap<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errors.compute(fieldError.getField(), (field, values) -> append(values, fieldError.getDefaultMessage()));
        }
        problem.setProperty("errors", errors);
        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ProblemDetail> handleMalformedJson(HttpMessageNotReadableException exception, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Malformed or unreadable JSON body.");
        problem.setTitle("Bad request");
        problem.setType(URI.create("https://teachgram.pro/problems/malformed-json"));
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("traceId", TraceIdFilter.currentTraceId());
        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ProblemDetail> handleUnexpected(Exception exception, HttpServletRequest request) {
        log.error("Unexpected API error. traceId={} path={}", TraceIdFilter.currentTraceId(), request.getRequestURI(), exception);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected error. Try again later."
        );
        problem.setTitle("Internal server error");
        problem.setType(URI.create("https://teachgram.pro/problems/internal-error"));
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("traceId", TraceIdFilter.currentTraceId());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

    private static String[] append(String[] current, String value) {
        if (current == null) {
            return new String[] { value };
        }
        String[] next = new String[current.length + 1];
        System.arraycopy(current, 0, next, 0, current.length);
        next[current.length] = value;
        return next;
    }

    private static String titleFor(HttpStatus status) {
        return switch (status) {
            case BAD_REQUEST -> "Bad request";
            case UNAUTHORIZED -> "Unauthorized";
            case FORBIDDEN -> "Forbidden";
            case NOT_FOUND -> "Not found";
            case CONFLICT -> "Conflict";
            case TOO_MANY_REQUESTS -> "Too many requests";
            default -> status.getReasonPhrase();
        };
    }
}
