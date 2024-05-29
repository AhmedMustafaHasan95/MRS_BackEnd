package com.ejada.meetingroomreservation.Exceptions;

import com.ejada.meetingroomreservation.DTO.ResponseData;
import com.ejada.meetingroomreservation.utils.ResponseRequestBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @Autowired
    protected ResponseRequestBuilder responseReqBuilder;
    private HttpStatus status;

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ResponseData> handleResponseStatusException(ResponseStatusException ex) {
        HttpStatus status = ex.getStatus();
        if (status.equals(HttpStatus.FORBIDDEN)) {
            status = HttpStatus.UNAUTHORIZED;
        }
        return ResponseEntity.status(status).body(wrapResponse(ex));
    }

    /* @ExceptionHandler(UserAuthenticationException.class)
     public ResponseEntity<ResponseData> handleUserAuthenticationException(UserAuthenticationException ex) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(wrapResponse(ex, ex.getCode()));
     }*/

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseData> handleConstraintViolationException(ConstraintViolationException ex) {
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String attributeName = violation.getPropertyPath().toString();
            return ResponseEntity.badRequest().body(wrapResponse(new Exception("Database ConstraintViolation Error due to : " +
                    "" + attributeName)));
        }
        return ResponseEntity.badRequest().body(wrapResponse(ex));
    }
/*
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseData> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(wrapResponse(ex.getMessage() + " due to : you don't have the authority to access this resource !!", HttpStatus.FORBIDDEN.value() + ""));
    }*/

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseData> handleException(Exception ex) {
        /*if (ex instanceof UserAuthenticationException) {
            handleUserAuthenticationException((UserAuthenticationException) ex);
        }*/
        return wrapException(ex);
    }

    private ResponseEntity<ResponseData> wrapException(Exception ex) {
        return ResponseEntity.badRequest().body(wrapResponse(ex));
    }

    private ResponseData wrapResponse(Exception ex) {
        ResponseData apiResponse = responseReqBuilder.wrapFailureResponse(ex.getMessage());
        logger.error(ex.getCause());
        ex.printStackTrace();
        return apiResponse;
    }

    private ResponseData wrapResponse(Exception ex, String status) {
        ResponseData apiResponse = responseReqBuilder.wrapFailureResponseWithCode(ex.getMessage(), status);
        logger.error(ex.getMessage());
        ex.printStackTrace();
        return apiResponse;
    }

    private ResponseData wrapResponse(String message, String status) {
        ResponseData apiResponse = responseReqBuilder.wrapFailureResponseWithCode(message, status);
        logger.error(message);
        return apiResponse;
    }
}
