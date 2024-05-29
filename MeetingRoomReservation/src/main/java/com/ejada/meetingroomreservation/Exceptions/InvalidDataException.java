package com.ejada.meetingroomreservation.Exceptions;

public class InvalidDataException extends RuntimeException {
    private String name;
    private String value;

    public <T> InvalidDataException(String name, Object value) {
        super(name + "  [" + value + "] Not Valid");
        this.name = name;
        this.value = (value == null) ? "null" : value.toString();
    }

    public InvalidDataException(String message) {
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

    @Override
    public String toString() {
        return "InvalidDataException{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}