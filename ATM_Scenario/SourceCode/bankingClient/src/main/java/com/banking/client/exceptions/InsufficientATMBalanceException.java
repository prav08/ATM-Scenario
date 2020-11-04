package com.banking.client.exceptions;

import lombok.Getter;
import lombok.Setter;

public class InsufficientATMBalanceException extends RuntimeException {
  @Getter @Setter private String message;

  public InsufficientATMBalanceException(final String message) {
    super(message);
    this.message = message;
  }
}
