package com.vr.mini.autorizador.miniautorizador.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.vr.mini.autorizador.miniautorizador.dto.request.CardRequestDTO;
import com.vr.mini.autorizador.miniautorizador.dto.response.CardResponseDTO;
import com.vr.mini.autorizador.miniautorizador.model.Card;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class CardMapperTest {

  private final CardMapper cardMapper = CardMapper.INSTANCE;
  private static final String CARD_NUMBER = "6549873025634501";
  private static final String PASSWORD = "1234";

  @Test
  void mapCardRequestDTOToEntity() {

    CardRequestDTO dto = CardRequestDTO.builder().number(CARD_NUMBER).password(PASSWORD).build();

    Card card = cardMapper.toEntity(dto);

    assertNotNull(card.getId());
    assertEquals(CARD_NUMBER, card.getNumber());
    assertEquals(PASSWORD, card.getPassword());
    assertNull(card.getBalance());
  }

  @Test
  void mapCardToResponseDTO() {
    Card card = new Card();
    card.setId("123456");
    card.setNumber(CARD_NUMBER);
    card.setBalance(BigDecimal.valueOf(1000));
    card.setPassword(PASSWORD);

    CardResponseDTO responseDTO = cardMapper.toResponse(card);

    assertEquals(CARD_NUMBER, responseDTO.getNumber());
    assertEquals(PASSWORD, responseDTO.getPassword());
  }

  @Test
  void mapEmptyCardRequestDTOToEntity() {
    CardRequestDTO dto = new CardRequestDTO();

    Card card = cardMapper.toEntity(dto);

    assertNotNull(card.getId());
    assertNull(card.getNumber());
    assertNull(card.getBalance());
    assertNull(card.getPassword());
  }

  @Test
  void mapEmptyCardToResponseDTO() {
    Card card = new Card();

    CardResponseDTO responseDTO = cardMapper.toResponse(card);

    assertNull(responseDTO.getNumber());
    assertNull(responseDTO.getPassword());
  }

  @Test
  void mapNullCardToResponseDTO() {
    CardResponseDTO responseDTO = cardMapper.toResponse(null);
    assertNull(responseDTO);
  }

  @Test
  void mapNullCardRequestDTOToEntity() {
    Card card = cardMapper.toEntity(null);
    assertNull(card);
  }
}
