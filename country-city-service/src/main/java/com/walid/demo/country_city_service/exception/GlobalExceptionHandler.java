package com.walid.demo.country_city_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.walid.demo.country_city_service.exception.filter.CorrelationIdFilter;
import com.walid.demo.country_city_service.exception.model.ErrorCode;
import com.walid.demo.country_city_service.exception.model.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

        private String getCorrelationId(HttpServletRequest request) {
                Object id = request.getAttribute(CorrelationIdFilter.HEADER);
                return id != null ? id.toString() : null;
        }

        // ================= NOT FOUND =================
        @ExceptionHandler(NotFoundException.class)
        public ResponseEntity<ErrorResponse> handleNotFound(
                        NotFoundException ex,
                        HttpServletRequest request) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                                new ErrorResponse(
                                                HttpStatus.NOT_FOUND.value(),
                                                ErrorCode.NOT_FOUND,
                                                ex.getMessage(),
                                                request.getRequestURI(),
                                                System.currentTimeMillis(),
                                                getCorrelationId(request)));
        }

        // ================= Bad Request =================
        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<ErrorResponse> handleBadRequest(
                        BadRequestException ex,
                        HttpServletRequest request) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                                new ErrorResponse(
                                                HttpStatus.BAD_REQUEST.value(),
                                                ErrorCode.BAD_REQUEST,
                                                ex.getMessage(),
                                                request.getRequestURI(),
                                                System.currentTimeMillis(),
                                                getCorrelationId(request)));
        }

        // ================= Illegal Argument Exception =================

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgument(
                        IllegalArgumentException ex,
                        HttpServletRequest request) {
                return ResponseEntity.badRequest().body(
                                new ErrorResponse(
                                                HttpStatus.BAD_REQUEST.value(),
                                                ErrorCode.BAD_REQUEST,
                                                ex.getMessage(),
                                                request.getRequestURI(),
                                                System.currentTimeMillis(),
                                                getCorrelationId(request)));
        }

        // ================= VALIDATION =================
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidation(
                        MethodArgumentNotValidException ex,
                        HttpServletRequest request) {

                String message = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                                .reduce((a, b) -> a + ", " + b)
                                .orElse("Validation error");

                return ResponseEntity.badRequest().body(
                                new ErrorResponse(
                                                HttpStatus.BAD_REQUEST.value(),
                                                ErrorCode.VALIDATION_ERROR,
                                                message,
                                                request.getRequestURI(),
                                                System.currentTimeMillis(),
                                                getCorrelationId(request)));
        }

        // ================= GENERIC =================
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGeneric(
                        Exception ex,
                        HttpServletRequest request) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                                new ErrorResponse(
                                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                ErrorCode.INTERNAL_ERROR,
                                                ex.getMessage(),
                                                request.getRequestURI(),
                                                System.currentTimeMillis(),
                                                getCorrelationId(request)));
        }

}