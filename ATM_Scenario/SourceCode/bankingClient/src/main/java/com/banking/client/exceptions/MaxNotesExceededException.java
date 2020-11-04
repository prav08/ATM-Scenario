package com.banking.client.exceptions;

import lombok.Getter;
import lombok.Setter;

public class MaxNotesExceededException extends RuntimeException {
    @Getter
    @Setter
    private String message;

    public MaxNotesExceededException(final String message) {
	super(message);
	this.message = message;
    }
}
