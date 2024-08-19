package com.vr.mini.autorizador.miniautorizador.service;

import com.vr.mini.autorizador.miniautorizador.dto.request.CardRequestDTO;
import com.vr.mini.autorizador.miniautorizador.exception.CardAlreadyExistsException;
import com.vr.mini.autorizador.miniautorizador.exception.CardNotFoundException;
import com.vr.mini.autorizador.miniautorizador.mapper.CardMapper;
import com.vr.mini.autorizador.miniautorizador.model.Card;
import com.vr.mini.autorizador.miniautorizador.repository.CardRepository;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CardService {
  private final CardRepository cardRepository;

  @Autowired
  public CardService(CardRepository cardRepository) {
    this.cardRepository = cardRepository;
  }

  @Transactional
  public Card create(CardRequestDTO request) {
    Card card = CardMapper.INSTANCE.toEntity(request);

    cardRepository
        .findByNumber(card.getNumber())
        .ifPresent(
            existingCard -> {
              log.error("Card with number {} already exists", card.getNumber());
              throw new CardAlreadyExistsException(CardMapper.INSTANCE.toResponse(existingCard));
            });

    card.setBalance(BigDecimal.valueOf(500));
    return cardRepository.save(card);
  }

  public BigDecimal getBalance(String number) {
    Card card =
        cardRepository
            .findByNumber(number)
            .orElseThrow(
                () -> {
                  log.error("Card with number {} not found", number);
                  return new CardNotFoundException("");
                });
    return card.getBalance();
  }
}
