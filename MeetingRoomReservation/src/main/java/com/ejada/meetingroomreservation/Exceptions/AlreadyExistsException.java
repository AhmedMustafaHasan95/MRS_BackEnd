package com.ejada.meetingroomreservation.Exceptions;

public class AlreadyExistsException extends Exception {

    private String name;
    private String value;


    public AlreadyExistsException(String message) {
        super(message);
    }

    public <T> AlreadyExistsException(String name, String value) {
        super(name + " [" + value + "] already exists");
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public <T> void setValue(String value) {
        this.value = value;
    }
}
