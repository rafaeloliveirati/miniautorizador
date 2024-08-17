package com.vr.mini.autorizador.miniautorizador.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({
    CardNotFoundException.class,
    CardAlreadyExistsException.class,
    InsufficientBalanceException.class,
    InvalidPasswordException.class,
    TransactionInProgressException.class
  })
  public ResponseEntity<String> handleException(RuntimeException ex) {
    HttpStatus status =
        switch (ex.getClass().getSimpleName()) {
          case "CardNotFoundException" -> HttpStatus.NOT_FOUND;
          case "CardAlreadyExistsException",
                  "InsufficientBalanceException",
                  "InvalidPasswordException",
                  "TransactionInProgressException" ->
              HttpStatus.UNPROCESSABLE_ENTITY;
          default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    return ResponseEntity.status(status).body(ex.getMessage());
  }
}
