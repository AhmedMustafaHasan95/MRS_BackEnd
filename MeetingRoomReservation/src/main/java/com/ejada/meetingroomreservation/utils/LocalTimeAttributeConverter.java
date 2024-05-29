package com.ejada.meetingroomreservation.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Time;
import java.time.LocalTime;

@Converter(autoApply = true)
public class LocalTimeAttributeConverter implements AttributeConverter<LocalTime, Time> {

    @Override
    public Time convertToDatabaseColumn(LocalTime localTime) {
        return localTime != null ? Time.valueOf(localTime) : null;
    }

    @Override
    public LocalTime convertToEntityAttribute(Time time) {
        return time != null ? time.toLocalTime() : null;
    }
}