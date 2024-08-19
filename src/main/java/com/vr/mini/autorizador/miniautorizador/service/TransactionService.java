package com.vr.mini.autorizador.miniautorizador.service;

import static com.vr.mini.autorizador.miniautorizador.ErrorMessages.CARD_NOT_FOUND;

import com.vr.mini.autorizador.miniautorizador.dto.request.TransactionRequestDTO;
import com.vr.mini.autorizador.miniautorizador.exception.CardNotFoundException;
import com.vr.mini.autorizador.miniautorizador.exception.InsufficientBalanceException;
import com.vr.mini.autorizador.miniautorizador.exception.InvalidPasswordException;
import com.vr.mini.autorizador.miniautorizador.exception.TransactionInProgressException;
import com.vr.mini.autorizador.miniautorizador.model.Card;
import com.vr.mini.autorizador.miniautorizador.repository.CardRepository;
import com.vr.mini.autorizador.miniautorizador.util.RedisUtil;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionService {

  private final CardRepository cardRepository;
  private final RedisUtil redisUtil;

  @Autowired
  public TransactionService(CardRepository cardRepository, RedisUtil redisUtil) {
    this.cardRepository = cardRepository;
    this.redisUtil = redisUtil;
  }

  public String performTransaction(TransactionRequestDTO request) {
    log.debug("Starting perform transaction for card: {}", request.getNumber());

    Card card = getCardByRequest(request);

    validate(request, card);

    redisUtil.set(request.getNumber(), "processing");

    card.setBalance(card.getBalance().subtract(request.getValue()));
    cardRepository.save(card);

    log.debug("Transaction performed successfully for card with number {}", request.getNumber());

    redisUtil.delete(request.getNumber());
    return "OK";
  }

  private void validate(TransactionRequestDTO request, Card card) {
    Optional.ofNullable(card.getPassword())
        .filter(password -> password.equals(request.getPassword()))
        .orElseThrow(
            () -> {
              log.error("Invalid password for card with number {}", request.getNumber());
              throw new InvalidPasswordException();
            });

    Optional.of(redisUtil.exists(request.getNumber()))
        .filter(exists -> !exists)
        .orElseThrow(
            () -> {
              log.error("Transaction already in progress for card; {}", request.getNumber());
              throw new TransactionInProgressException();
            });

    Optional.of(card.getBalance())
        .filter(balance -> balance.compareTo(request.getValue()) >= 0)
        .orElseThrow(
            () -> {
              log.error("Insufficient balance for card: {}", request.getNumber());
              throw new InsufficientBalanceException();
            });
  }

  private Card getCardByRequest(TransactionRequestDTO request) {
    return cardRepository
        .findByNumber(request.getNumber())
        .orElseThrow(
            () -> {
              log.error("Card with number {} not found", request.getNumber());
              throw new CardNotFoundException(CARD_NOT_FOUND.getMessage());
            });
  }
}
