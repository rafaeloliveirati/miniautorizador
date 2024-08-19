package com.vr.mini.autorizador.miniautorizador.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.vr.mini.autorizador.miniautorizador.dto.request.CardRequestDTO;
import com.vr.mini.autorizador.miniautorizador.exception.CardAlreadyExistsException;
import com.vr.mini.autorizador.miniautorizador.exception.CardNotFoundException;
import com.vr.mini.autorizador.miniautorizador.model.Card;
import com.vr.mini.autorizador.miniautorizador.repository.CardRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CardServiceTest {

  @Mock private CardRepository cardRepository;

  @InjectMocks private CardService cardService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  private static final String CARD_NUMBER = "6549873025634501";
  private static final String PASSWORD = "1234";

  @Test
  void createCardSuccessfully() {
    Card card = getCard();
    CardRequestDTO cardRequest =
        CardRequestDTO.builder().number(card.getNumber()).password(card.getPassword()).build();

    when(cardRepository.findByNumber(card.getNumber())).thenReturn(Optional.empty());
    when(cardRepository.save(any(Card.class))).thenReturn(card);

    Card createdCard = cardService.create(cardRequest);

    assertNotNull(createdCard);
    assertEquals(card.getNumber(), createdCard.getNumber());
    assertEquals(card.getPassword(), createdCard.getPassword());

    verify(cardRepository).save(any(Card.class));
  }

  @Test
  void createCardThrowsCardAlreadyExistsException() {
    Card card = getCard();
    CardRequestDTO cardRequest =
        CardRequestDTO.builder().number(card.getNumber()).password(card.getPassword()).build();

    when(cardRepository.findByNumber(card.getNumber())).thenReturn(Optional.of(card));

    assertThrows(CardAlreadyExistsException.class, () -> cardService.create(cardRequest));
  }

  @Test
  void getBalanceSuccessfully() {
    Card card = getCard();

    when(cardRepository.findByNumber(card.getNumber())).thenReturn(Optional.of(card));

    BigDecimal balance = cardService.getBalance(card.getNumber());

    assertNotNull(balance);
    assertEquals(BigDecimal.valueOf(500), balance);
  }

  @Test
  void getBalanceThrowsCardNotFoundException() {
    when(cardRepository.findByNumber(CARD_NUMBER)).thenReturn(Optional.empty());
    assertThrows(CardNotFoundException.class, () -> cardService.getBalance(CARD_NUMBER));
  }

  private Card getCard() {
    Card card = new Card();
    card.setNumber(CARD_NUMBER);
    card.setPassword(PASSWORD);
    card.setBalance(BigDecimal.valueOf(500));
    return card;
  }
}
