package com.vr.mini.autorizador.miniautorizador.exception;

import com.vr.mini.autorizador.miniautorizador.dto.response.CardResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CardAlreadyExistsException.class)
  public ResponseEntity<CardResponseDTO> handleCardAlreadyExistsException(
      CardAlreadyExistsException ex) {
    return new ResponseEntity<>(ex.getCardResponse(), HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler({
    CardNotFoundException.class,
    InsufficientBalanceException.class,
    InvalidPasswordException.class,
    TransactionInProgressException.class
  })
  public ResponseEntity<String> handleException(RuntimeException ex) {
    HttpStatus status =
        switch (ex.getClass().getSimpleName()) {
          case "CardNotFoundException" -> HttpStatus.NOT_FOUND;
          case "InsufficientBalanceException",
                  "InvalidPasswordException",
                  "TransactionInProgressException" ->
              HttpStatus.UNPROCESSABLE_ENTITY;
          default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    return ResponseEntity.status(status).body(ex.getMessage());
  }
}
