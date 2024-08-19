package com.vr.mini.autorizador.miniautorizador.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.mini.autorizador.miniautorizador.dto.request.CardRequestDTO;
import com.vr.mini.autorizador.miniautorizador.model.Card;
import com.vr.mini.autorizador.miniautorizador.repository.CardRepository;
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
class CardControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private CardRepository cardRepository;

  @BeforeEach
  void setUp() {
    cardRepository.deleteAll();
  }

  private static final String CARDS_CREATE_URL = "/cartoes";
  private static final String BALANCE_URL = "/cartoes/%s";

  @Test
  void createCardSuccessfully() throws Exception {
    String number = "6549873025634501";
    String password = "1234";

    CardRequestDTO request = CardRequestDTO.builder().number(number).password(password).build();

    mockMvc
        .perform(
            post(CARDS_CREATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.numeroCartao").value(number))
        .andExpect(jsonPath("$.senha").value(password));
  }

  @Test
  void getCardBalanceSuccessfully() throws Exception {
    String number = "6549873025634501";
    String password = "1234";
    saveCard(number, password);

    mockMvc
        .perform(get(String.format(BALANCE_URL, number)))
        .andExpect(status().isOk())
        .andExpect(content().string("500"));
  }

  @Test
  void getCardBalanceCardNotFound() throws Exception {
    String number = "6549873025634501";
    mockMvc.perform(get(String.format(BALANCE_URL, number))).andExpect(status().isNotFound());
  }

  @Test
  void getCardBalanceCardAlreadyExistsException() throws Exception {
    String number = "6549873025634501";
    String password = "1234";
    saveCard(number, password);

    CardRequestDTO request = CardRequestDTO.builder().number(number).password(password).build();

    mockMvc
        .perform(
            post(CARDS_CREATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.numeroCartao").value(number))
        .andExpect(jsonPath("$.senha").value(password));
  }

  private void saveCard(String number, String password) {
    Card card = new Card();
    card.setNumber(number);
    card.setPassword(password);
    card.setBalance(BigDecimal.valueOf(500));
    cardRepository.save(card);
  }
}
