package com.vr.mini.autorizador.miniautorizador.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.vr.mini.autorizador.miniautorizador.dto.request.TransactionRequestDTO;
import com.vr.mini.autorizador.miniautorizador.exception.*;
import com.vr.mini.autorizador.miniautorizador.model.Card;
import com.vr.mini.autorizador.miniautorizador.repository.CardRepository;
import com.vr.mini.autorizador.miniautorizador.util.RedisUtil;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TransactionServiceTest {

  @Mock private CardRepository cardRepository;
  @Mock private RedisUtil redisUtil;
  @InjectMocks private TransactionService transactionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  private static final String CARD_NUMBER = "6549873025634501";
  private static final String PASSWORD = "1234";

  @Test
  void performTransactionSuccessfully() {
    Card card = getCard();
    TransactionRequestDTO request =
        TransactionRequestDTO.builder()
            .number(CARD_NUMBER)
            .password(PASSWORD)
            .value(BigDecimal.valueOf(100))
            .build();

    when(cardRepository.findByNumber(request.getNumber())).thenReturn(Optional.of(card));
    when(redisUtil.exists(request.getNumber())).thenReturn(false);

    String result = transactionService.performTransaction(request);

    assertEquals("OK", result);
    assertEquals(BigDecimal.valueOf(400), card.getBalance());
    verify(cardRepository).save(card);
    verify(redisUtil).set(request.getNumber(), "processing");
    verify(redisUtil).delete(request.getNumber());
  }

  @Test
  void performTransactionThrowsCardNotFoundException() {
    TransactionRequestDTO request =
        TransactionRequestDTO.builder()
            .number(CARD_NUMBER)
            .password(PASSWORD)
            .value(BigDecimal.valueOf(100))
            .build();

    when(cardRepository.findByNumber(request.getNumber())).thenReturn(Optional.empty());

    assertThrows(CardNotFoundException.class, () -> transactionService.performTransaction(request));
  }

  @Test
  void performTransactionThrowsInvalidPasswordException() {
    Card card = getCard();
    TransactionRequestDTO request =
        TransactionRequestDTO.builder()
            .number(CARD_NUMBER)
            .password("wrong_password")
            .value(BigDecimal.valueOf(100))
            .build();

    when(cardRepository.findByNumber(request.getNumber())).thenReturn(Optional.of(card));

    assertThrows(
        InvalidPasswordException.class, () -> transactionService.performTransaction(request));
  }

  @Test
  void performTransactionThrowsTransactionInProgressException() {
    Card card = getCard();
    TransactionRequestDTO request =
        TransactionRequestDTO.builder()
            .number(CARD_NUMBER)
            .password(PASSWORD)
            .value(BigDecimal.valueOf(100))
            .build();

    when(cardRepository.findByNumber(request.getNumber())).thenReturn(Optional.of(card));
    when(redisUtil.exists(request.getNumber())).thenReturn(true);

    assertThrows(
        TransactionInProgressException.class, () -> transactionService.performTransaction(request));
  }

  @Test
  void performTransactionThrowsInsufficientBalanceException() {
    Card card = getCard();
    TransactionRequestDTO request =
        TransactionRequestDTO.builder()
            .number(CARD_NUMBER)
            .password(PASSWORD)
            .value(BigDecimal.valueOf(600))
            .build();

    when(cardRepository.findByNumber(request.getNumber())).thenReturn(Optional.of(card));
    when(redisUtil.exists(request.getNumber())).thenReturn(false);

    assertThrows(
        InsufficientBalanceException.class, () -> transactionService.performTransaction(request));
  }

  private Card getCard() {
    Card card = new Card();
    card.setNumber(CARD_NUMBER);
    card.setPassword(PASSWORD);
    card.setBalance(BigDecimal.valueOf(500));
    return card;
  }
}
