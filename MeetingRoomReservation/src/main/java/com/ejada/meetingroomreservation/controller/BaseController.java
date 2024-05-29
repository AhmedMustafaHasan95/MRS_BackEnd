package com.ejada.meetingroomreservation.controller;

import com.ejada.meetingroomreservation.DTO.ApiStatusDTO;
import com.ejada.meetingroomreservation.DTO.ErrorDTO;
import com.ejada.meetingroomreservation.DTO.ResponseData;
import com.ejada.meetingroomreservation.Exceptions.AlreadyExistsException;
import com.ejada.meetingroomreservation.Exceptions.EntityNotFoundException;
import com.ejada.meetingroomreservation.Exceptions.InvalidDataException;
import com.ejada.meetingroomreservation.Exceptions.ValidationsException;
import com.ejada.meetingroomreservation.utils.ResponseRequestBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public abstract class BaseController {
    private static final Logger logger = LogManager.getLogger(BaseController.class);

    @Autowired
    protected ResponseRequestBuilder responseReqBuilder;

    protected ResponseEntity<ResponseData> wrapException(Exception ex, Logger logger) {
        if (ex instanceof ValidationsException) {
            return wrapValidationsException(((ValidationsException) ex), logger);
        } else if (ex instanceof NullPointerException) {
            return wrapNullPointerException((NullPointerException) ex, logger);
        } else if (ex instanceof AlreadyExistsException) {
            return wrapAlreadyExistsException(((AlreadyExistsException) ex));
        } else if (ex instanceof EntityNotFoundException) {
            return wrapEntityNotFoundException(((EntityNotFoundException) ex));
        } else if (ex instanceof InvalidDataException) {
            return wrapInvalidDataException(((InvalidDataException) ex));
        }/* else if (ex instanceof UserAuthenticationException) {
            return wrapUserAuthenticationException(((UserAuthenticationException) ex));
        }*/
        ResponseData apiResponse = responseReqBuilder.wrapFailureResponse(ex.getMessage());
        logger.error(ex.getMessage());
        ex.printStackTrace();
        return ResponseEntity.badRequest().body(apiResponse);
    }

    private ResponseEntity<ResponseData> wrapAlreadyExistsException(AlreadyExistsException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("msgCode", "response.error.alreadyExists");
        errors.put("name", ex.getName());
        if (!ex.getValue().isBlank()) {
            errors.put("value", ex.getValue());
        }
        ResponseData apiResponse = new ResponseData();
        apiResponse.setStatus(new ApiStatusDTO(errors));
        logger.error("Already exists " + ex.getName() + " : " + ex.getValue());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    private ResponseEntity<ResponseData> wrapEntityNotFoundException(EntityNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("msgCode", "response.error.notFound");
        errors.put("name", ex.getName());
        if (!ex.getValue().isBlank())
            errors.put("value", ex.getValue());

        ResponseData apiResponse = new ResponseData();
        apiResponse.setStatus(new ApiStatusDTO(errors));
        logger.error("Not Found " + ex.getName() + " : " + ex.getValue());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    private ResponseEntity<ResponseData> wrapInvalidDataException(InvalidDataException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("name", ex.getName());
        if (!ex.getValue().isBlank()) {
            errors.put("value", ex.getValue());
            errors.put("msgCode", "response.error.invalid");
        } else {
            errors.put("msgCode", "request.error.invalid");
        }
        ResponseData apiResponse = new ResponseData();
        apiResponse.setStatus(new ApiStatusDTO(errors));
        logger.error("Not Found " + ex.getName() + " : " + ex.getValue());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    protected ResponseEntity<ResponseData> wrapException(String message, Logger logger) {
        ResponseData apiResponse = responseReqBuilder.wrapFailureResponse(message);
        logger.error("ValidationsErrors due to " + message);
        return ResponseEntity.badRequest().body(apiResponse);
    }

    protected ResponseEntity<ResponseData> wrapValidationsException(ValidationsException ex, Logger logger) {
        ResponseData apiResponse = null;
        if (ex.getErrors() != null && !ex.getErrors().isEmpty()) {
            apiResponse = responseReqBuilder.wrapFailureResponse(ex.getErrors());
            logger.error("Missing attributes needed [" + StringUtils.join(ex.getErrors().stream().map(ErrorDTO::toString).collect(Collectors.toList()), ',') + "]");
        } else {
            return wrapException(ex.getMessage(), logger);
        }
        return ResponseEntity.badRequest().body(apiResponse);
    }

    protected ResponseEntity<ResponseData> wrapNullPointerException(NullPointerException ex, Logger logger) {
        String message = "Null Pointer Exception occurred due to : " + ex.getCause();
        ResponseData apiResponse = responseReqBuilder.wrapFailureResponse(message);
        logger.error(message);
        ex.printStackTrace();
        return ResponseEntity.badRequest().body(apiResponse);
    }

}