package com.ejada.meetingroomreservation.Exceptions;

import com.ejada.meetingroomreservation.DTO.ErrorDTO;

import java.util.ArrayList;
import java.util.List;

public class ValidationsException extends RuntimeException {
    private List<ErrorDTO> errors;
    private String attributeName;
    private String msgCode;
    private boolean attrValidation;

    public ValidationsException(String message) {
        super(message);
    }

    public ValidationsException(String attributeName, String msgCode, boolean attrValidation) {
        this.attributeName = attributeName;
        this.msgCode = msgCode;
        this.attrValidation = attrValidation;
    }

    public ValidationsException(List<ErrorDTO> errors) {
        this.errors = errors;
    }

    public ValidationsException(ErrorDTO error) {
        this.errors = new ArrayList<>();
        this.errors.add(error);
    }

    public List<ErrorDTO> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorDTO> errors) {
        this.errors = errors;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public boolean isAttrValidation() {
        return attrValidation;
    }

    public void setAttrValidation(boolean attrValidation) {
        this.attrValidation = attrValidation;
    }
}
