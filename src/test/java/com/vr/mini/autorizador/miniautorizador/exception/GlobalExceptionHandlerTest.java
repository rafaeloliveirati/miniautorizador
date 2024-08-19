package com.vr.mini.autorizador.miniautorizador.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.vr.mini.autorizador.miniautorizador.dto.response.CardResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

  @InjectMocks private GlobalExceptionHandler globalExceptionHandler;

  @Mock private CardAlreadyExistsException cardAlreadyExistsException;

  @Mock private CardNotFoundException cardNotFoundException;

  @Mock private InsufficientBalanceException insufficientBalanceException;

  @Mock private InvalidPasswordException invalidPasswordException;

  @Mock private TransactionInProgressException transactionInProgressException;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void handleCardAlreadyExistsExceptionReturnsUnprocessableEntity() {
    CardResponseDTO cardResponseDTO = new CardResponseDTO();
    when(cardAlreadyExistsException.getCardResponse()).thenReturn(cardResponseDTO);

    ResponseEntity<CardResponseDTO> response =
        globalExceptionHandler.handleCardAlreadyExistsException(cardAlreadyExistsException);

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    assertEquals(cardResponseDTO, response.getBody());
  }

  @Test
  void handleCardNotFoundExceptionReturnsNotFound() {
    when(cardNotFoundException.getMessage()).thenReturn("Card not found");

    ResponseEntity<String> response = globalExceptionHandler.handleException(cardNotFoundException);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Card not found", response.getBody());
  }

  @Test
  void handleInsufficientBalanceExceptionReturnsUnprocessableEntity() {
    when(insufficientBalanceException.getMessage()).thenReturn("Insufficient balance");

    ResponseEntity<String> response =
        globalExceptionHandler.handleException(insufficientBalanceException);

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    assertEquals("Insufficient balance", response.getBody());
  }

  @Test
  void handleInvalidPasswordExceptionReturnsUnprocessableEntity() {
    when(invalidPasswordException.getMessage()).thenReturn("Invalid password");

    ResponseEntity<String> response =
        globalExceptionHandler.handleException(invalidPasswordException);

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    assertEquals("Invalid password", response.getBody());
  }

  @Test
  void handleTransactionInProgressExceptionReturnsUnprocessableEntity() {
    when(transactionInProgressException.getMessage()).thenReturn("Transaction in progress");

    ResponseEntity<String> response =
        globalExceptionHandler.handleException(transactionInProgressException);

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    assertEquals("Transaction in progress", response.getBody());
  }

  @Test
  void handleUnknownExceptionReturnsInternalServerError() {
    RuntimeException unknownException = new RuntimeException("Unknown error");

    ResponseEntity<String> response = globalExceptionHandler.handleException(unknownException);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("Unknown error", response.getBody());
  }
}
