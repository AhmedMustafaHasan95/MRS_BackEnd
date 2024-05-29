package com.ejada.meetingroomreservation.auth.exceptions;

public class UserAuthenticationException extends RuntimeException{
    private String code = "405";
    private String msgCode;
    private String value;
    public UserAuthenticationException(String message){
        super(message);
    }

    public UserAuthenticationException(String message, String code) {
        super(message);
        this.code = code;
    }

    public UserAuthenticationException(String message, String msgCode, String value) {
        super(message);
        this.msgCode = msgCode;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
