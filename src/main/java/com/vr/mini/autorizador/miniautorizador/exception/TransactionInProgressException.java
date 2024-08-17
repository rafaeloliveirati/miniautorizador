package com.vr.mini.autorizador.miniautorizador.exception;

public class TransactionInProgressException extends RuntimeException {
  public TransactionInProgressException(String message) {
    super(message);
  }
}
