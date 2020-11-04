package com.banking.client.exceptions;

import lombok.Getter;
import lombok.Setter;

public class AccountNotFoundException  extends RuntimeException {
    @Getter
    @Setter
    private String message;

    public AccountNotFoundException(final String message) {
	super(message);
	this.message = message;
    }

}
