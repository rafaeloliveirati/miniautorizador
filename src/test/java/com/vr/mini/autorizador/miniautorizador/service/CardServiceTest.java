package com.vr.mini.autorizador.miniautorizador.service;

import com.vr.mini.autorizador.miniautorizador.dto.request.CardRequest;
import com.vr.mini.autorizador.miniautorizador.exception.CardAlreadyExistsException;
import com.vr.mini.autorizador.miniautorizador.exception.CardNotFoundException;
import com.vr.mini.autorizador.miniautorizador.model.Card;
import com.vr.mini.autorizador.miniautorizador.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CardServiceTest {

  @Mock private CardRepository cardRepository;

  @InjectMocks private CardService cardService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void createCardSuccessfully() {
    CardRequest cardRequest =
        CardRequest.builder().number("1234567890123456").password("1234").build();
    Card card = Card.builder().number("1234567890123456").password("1234").build();

    when(cardRepository.findByNumber(card.getNumber())).thenReturn(Optional.empty());
    when(cardRepository.save(any(Card.class))).thenReturn(card);

    Card createdCard = cardService.create(cardRequest);

    assertNotNull(createdCard);
    assertEquals(card.getNumber(), createdCard.getNumber());
    assertEquals(card.getPassword(), createdCard.getPassword());
    assertEquals(BigDecimal.valueOf(500), createdCard.getBalance());


    verify(cardRepository).save(any(Card.class));
  }

  @Test
  void createCardThrowsCardAlreadyExistsException() {
    CardRequest cardRequest =
        CardRequest.builder().number("1234567890123456").password("1234").build();
    Card card = Card.builder().number("1234567890123456").password("1234").build();

    when(cardRepository.findByNumber(card.getNumber())).thenReturn(Optional.of(card));

    assertThrows(CardAlreadyExistsException.class, () -> cardService.create(cardRequest));
  }

  @Test
  void createCardThrowsCardNotFoundException() {
    CardRequest cardRequest = CardRequest.builder().password("1234").build();

    assertThrows(CardNotFoundException.class, () -> cardService.create(cardRequest));
  }

  @Test
  void getBalanceSuccessfully() {
    Card card =
        Card.builder()
            .number("1234567890123456")
            .password("1234")
            .balance(BigDecimal.valueOf(500))
            .build();

    when(cardRepository.findByNumber(card.getNumber())).thenReturn(Optional.of(card));

    BigDecimal balance = cardService.getBalance(card.getNumber());

    assertNotNull(balance);
    assertEquals(BigDecimal.valueOf(500), balance);
  }

  @Test
  void getBalanceThrowsCardNotFoundException() {
    when(cardRepository.findByNumber("1234567890123456")).thenReturn(Optional.empty());

    assertThrows(CardNotFoundException.class, () -> cardService.getBalance("1234567890123456"));
  }
}
