package com.vr.mini.autorizador.miniautorizador.service;

import com.google.gson.Gson;
import com.vr.mini.autorizador.miniautorizador.exception.CardNotFoundException;
import com.vr.mini.autorizador.miniautorizador.mapper.CardMapper;
import com.vr.mini.autorizador.miniautorizador.dto.request.CardRequest;
import com.vr.mini.autorizador.miniautorizador.exception.CardAlreadyExistsException;
import com.vr.mini.autorizador.miniautorizador.model.Card;
import com.vr.mini.autorizador.miniautorizador.repository.CardRepository;

import java.math.BigDecimal;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CardService {
  private final CardRepository cardRepository;
  private final Gson gson = new Gson();

  @Autowired
  public CardService(CardRepository cardRepository) {
    this.cardRepository = cardRepository;
  }

  @Transactional
  public Card create(CardRequest cardRequest) {
    Card card =
        Optional.ofNullable(CardMapper.INSTANCE.toEntity(cardRequest))
            .orElseThrow(() -> new CardNotFoundException("Card not found"));

    cardRepository
        .findByNumber(card.getNumber())
        .ifPresent(
            existingCard -> {
              log.error("Card with number {} already exists", card.getNumber());
              throw new CardAlreadyExistsException(
                  gson.toJson(CardMapper.INSTANCE.toResponse(card)));
            });

    card.setBalance(BigDecimal.valueOf(500));
    return cardRepository.save(card);
  }

  public BigDecimal getBalance(String number) {
    Card card =
        cardRepository
            .findByNumber(number)
            .orElseThrow(() -> new CardNotFoundException("Card not found"));

    return card.getBalance();
  }
}
