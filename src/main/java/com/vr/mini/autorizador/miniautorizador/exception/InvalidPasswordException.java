package com.vr.mini.autorizador.miniautorizador.exception;


import static com.vr.mini.autorizador.miniautorizador.ErrorMessages.INVALID_PASSWORD;

public class InvalidPasswordException extends RuntimeException {
  public InvalidPasswordException() {
    super(INVALID_PASSWORD.getMessage());
  }
}
