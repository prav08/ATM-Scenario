package com.banking.server.exceptions;

import lombok.Getter;
import lombok.Setter;

public class InvalidBalanceException extends RuntimeException {
  @Getter @Setter private String message;

  public InvalidBalanceException(final String message) {
    super(message);
    this.message = message;
  }
}
