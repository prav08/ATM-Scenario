package com.banking.server.exceptions;

import lombok.Getter;
import lombok.Setter;

public class ResourceNotFoundException extends RuntimeException {
  @Getter @Setter private String message;

  public ResourceNotFoundException(final String message) {
    super(message);
    this.message = message;
  }
}
