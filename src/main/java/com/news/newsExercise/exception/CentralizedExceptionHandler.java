package com.news.newsExercise.exception;

import com.news.newsExercise.model.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CentralizedExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger loggerError = LoggerFactory.getLogger("loggerError");

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = "Content-Type '" + ex.getContentType() + "' not supported. Supported content types: " +
                ex.getSupportedMediaTypes().stream()
                        .map(MediaType::toString)
                        .collect(Collectors.joining(", "));
        loggerError.error(message);
        Error error = new Error();
        error.setMessage(message);
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        error.setStatusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        error.setErrorCode("UNSUPPORTED_MEDIA_TYPE");
        error.setDetails(ex.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(error);
    }

    @ExceptionHandler(NewsNotFoundException.class)
    public ResponseEntity<Object> NewsNotFoundHandler(NewsNotFoundException e) {
        loggerError.error(e.getMessage());
        Error error = new Error();
        error.setMessage(e.getMessage());
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.NOT_FOUND);
        error.setStatusCode(HttpStatus.NOT_FOUND.value());
        error.setErrorCode("NEWS_NOT_FOUND");
        error.setDetails(e.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Object> CategoryNotFoundHandler(CategoryNotFoundException e) {
        loggerError.error(e.getMessage());
        Error error = new Error();
        error.setMessage(e.getMessage());
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.NOT_FOUND);
        error.setStatusCode(HttpStatus.NOT_FOUND.value());
        error.setErrorCode("CATEGORY_NOT_FOUND");
        error.setDetails(e.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NewsDateTooEarlyException.class)
    public ResponseEntity<Object> NewsDateTooEarlyHandler(NewsDateTooEarlyException e) {
        loggerError.error(e.getMessage());
        Error error = new Error();
        error.setMessage(e.getMessage());
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.FORBIDDEN);
        error.setStatusCode(HttpStatus.FORBIDDEN.value());
        error.setErrorCode("DATE_TOO_EARLY");
        error.setDetails(e.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CategoryAndSubcategoryMismatchException.class)
    public ResponseEntity<Object> CategoryAndSubcategoryMismatchHandler(CategoryAndSubcategoryMismatchException e) {
        loggerError.error(e.getMessage());
        Error error = new Error();
        error.setMessage(e.getMessage());
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.FORBIDDEN);
        error.setStatusCode(HttpStatus.FORBIDDEN.value());
        error.setErrorCode("CATEGORY_AND_SUBCATEGORY_MISMATCH");
        error.setDetails(e.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
}
