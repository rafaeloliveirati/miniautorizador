package com.vr.mini.autorizador.miniautorizador;

import lombok.Getter;

@Getter
public enum ErrorMessages {
  INSUFFICIENT_BALANCE("SALDO_INSUFICIENTE"),
  INVALID_PASSWORD("SENHA_INVALIDA"),
  TRANSACTION_ALREADY_IN_PROGRESS("TRANSACTION_ALREADY_IN_PROGRESS"),
  CARD_NOT_FOUND("CARTAO_INEXISTENTE");

  private final String message;

  ErrorMessages(String message) {
    this.message = message;
  }
}
