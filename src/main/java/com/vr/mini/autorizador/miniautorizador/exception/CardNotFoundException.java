package com.vr.mini.autorizador.miniautorizador.exception;

public class CardNotFoundException extends RuntimeException {
  public CardNotFoundException(String message) {
    super(message);
  }
}