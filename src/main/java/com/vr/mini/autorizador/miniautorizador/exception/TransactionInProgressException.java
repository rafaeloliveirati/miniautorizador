package com.vr.mini.autorizador.miniautorizador.exception;


import static com.vr.mini.autorizador.miniautorizador.ErrorMessages.TRANSACTION_ALREADY_IN_PROGRESS;

public class TransactionInProgressException extends RuntimeException {
  public TransactionInProgressException() {
    super(TRANSACTION_ALREADY_IN_PROGRESS.getMessage());
  }
}
