package com.banking.client.exceptions;

import lombok.Getter;
import lombok.Setter;

public class InsufficientAccountBalanceException extends RuntimeException {
    @Getter
    @Setter
    private String message;

    public InsufficientAccountBalanceException(final String message) {
	super(message);
	this.message = message;
    }

}
