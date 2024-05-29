package com.ejada.meetingroomreservation.Exceptions;

public class EntityNotFoundException extends RuntimeException {
    private String name;
    private String value;

    public <T> EntityNotFoundException(String name, Object value) {
        super(name + " With ID (" + value + ") Not exists");
        this.name = name;
        this.value = value.toString();
    }

    public EntityNotFoundException(String message) {
        super(message);
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

    public void setValue(String value) {
        this.value = value;
    }
}
