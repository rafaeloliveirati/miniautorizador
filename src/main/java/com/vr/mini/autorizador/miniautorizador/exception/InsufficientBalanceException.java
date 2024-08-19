package com.vr.mini.autorizador.miniautorizador.exception;


import static com.vr.mini.autorizador.miniautorizador.ErrorMessages.INSUFFICIENT_BALANCE;

public class InsufficientBalanceException extends RuntimeException {
  public InsufficientBalanceException() {
    super(INSUFFICIENT_BALANCE.getMessage());
  }
}
