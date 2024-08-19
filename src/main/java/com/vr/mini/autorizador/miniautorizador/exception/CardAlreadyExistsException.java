package com.vr.mini.autorizador.miniautorizador.exception;

import com.vr.mini.autorizador.miniautorizador.dto.response.CardResponseDTO;
import lombok.Getter;

import java.io.Serial;

@Getter
public class CardAlreadyExistsException extends RuntimeException {
  @Serial private static final long serialVersionUID = 1L;
  private final CardResponseDTO cardResponse;

  public CardAlreadyExistsException(CardResponseDTO cardResponse) {
    super();
    this.cardResponse = cardResponse;
  }
}
