package com.vr.mini.autorizador.miniautorizador.controller;

import static com.vr.mini.autorizador.miniautorizador.ErrorMessages.CARD_NOT_FOUND;
import static com.vr.mini.autorizador.miniautorizador.ErrorMessages.INSUFFICIENT_BALANCE;
import static com.vr.mini.autorizador.miniautorizador.ErrorMessages.INVALID_PASSWORD;
import static com.vr.mini.autorizador.miniautorizador.ErrorMessages.TRANSACTION_ALREADY_IN_PROGRESS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.mini.autorizador.miniautorizador.dto.request.TransactionRequestDTO;
import com.vr.mini.autorizador.miniautorizador.model.Card;
import com.vr.mini.autorizador.miniautorizador.repository.CardRepository;
import com.vr.mini.autorizador.miniautorizador.util.RedisUtil;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private CardRepository cardRepository;

  @Autowired private RedisUtil redisUtil;

  private static final String TRANSACTIONS_URL = "/transacoes";

  @BeforeEach
  void setUp() {
    redisUtil.delete("6549873025634501");
    cardRepository.deleteAll();
  }

  private static final String CARD_NUMBER = "6549873025634501";
  private static final String PASSWORD = "1234";

  @Test
  void performTransactionSuccessfully() throws Exception {

    Card card = new Card();
    card.setNumber(CARD_NUMBER);
    card.setPassword(PASSWORD);
    card.setBalance(BigDecimal.valueOf(500));
    cardRepository.save(card);

    TransactionRequestDTO request =
        TransactionRequestDTO.builder()
            .number(CARD_NUMBER)
            .password(PASSWORD)
            .value(BigDecimal.valueOf(100))
            .build();

    mockMvc
        .perform(
            post(TRANSACTIONS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(content().string("OK"));
  }

  @Test
  void performTransactionInsufficientBalance() throws Exception {
    Card card = new Card();
    card.setNumber(CARD_NUMBER);
    card.setPassword(PASSWORD);
    card.setBalance(BigDecimal.valueOf(500));
    cardRepository.save(card);

    TransactionRequestDTO request =
        TransactionRequestDTO.builder()
            .number(CARD_NUMBER)
            .password(PASSWORD)
            .value(BigDecimal.valueOf(1000))
            .build();

    mockMvc
        .perform(
            post(TRANSACTIONS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().string(INSUFFICIENT_BALANCE.getMessage()));
  }

  @Test
  void performTransactionInvalidPassword() throws Exception {
    Card card = new Card();
    card.setNumber(CARD_NUMBER);
    card.setPassword(PASSWORD);
    card.setBalance(BigDecimal.valueOf(500));
    cardRepository.save(card);

    TransactionRequestDTO request =
        TransactionRequestDTO.builder()
            .number(CARD_NUMBER)
            .password("invalid-password")
            .value(BigDecimal.valueOf(1000))
            .build();

    mockMvc
        .perform(
            post(TRANSACTIONS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().string(INVALID_PASSWORD.getMessage()));
  }

  @Test
  void performTransactionCardNotFound() throws Exception {

    TransactionRequestDTO request =
        TransactionRequestDTO.builder()
            .number(CARD_NUMBER)
            .password(PASSWORD)
            .value(BigDecimal.valueOf(1000))
            .build();

    mockMvc
        .perform(
            post(TRANSACTIONS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(content().string(CARD_NOT_FOUND.getMessage()));
  }

  @Test
  void performTransactionInProgressException() throws Exception {
    redisUtil.set(CARD_NUMBER, "processing");

    Card card = new Card();
    card.setNumber(CARD_NUMBER);
    card.setPassword(PASSWORD);
    card.setBalance(BigDecimal.valueOf(500));
    cardRepository.save(card);

    TransactionRequestDTO request =
        TransactionRequestDTO.builder()
            .number(CARD_NUMBER)
            .password(PASSWORD)
            .value(BigDecimal.valueOf(1000))
            .build();

    mockMvc
        .perform(
            post(TRANSACTIONS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().string(TRANSACTION_ALREADY_IN_PROGRESS.getMessage()));
  }
}
