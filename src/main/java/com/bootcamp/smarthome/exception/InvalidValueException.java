package com.bootcamp.smarthome.exception;
import lombok.Getter;

public class InvalidValueException extends HomeAutomationException{

    @Getter
    private final String field;
    @Getter
    private final Object value;
    @Getter
    private final String constraint;

    public InvalidValueException(String field, Object value, String constraint){
        super(String.format(
                "Invalid value for '%s': %s (constraint: %s)",
                field, value, constraint
        ));
        this.field = field;
        this.value = value;
        this.constraint = constraint;
    }
}
