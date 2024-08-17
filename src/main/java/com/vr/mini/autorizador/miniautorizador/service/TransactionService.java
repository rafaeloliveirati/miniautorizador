package com.vr.mini.autorizador.miniautorizador.service;

import com.vr.mini.autorizador.miniautorizador.dto.request.TransactionRequest;
import com.vr.mini.autorizador.miniautorizador.exception.CardNotFoundException;
import com.vr.mini.autorizador.miniautorizador.exception.InsufficientBalanceException;
import com.vr.mini.autorizador.miniautorizador.exception.InvalidPasswordException;
import com.vr.mini.autorizador.miniautorizador.exception.TransactionInProgressException;
import com.vr.mini.autorizador.miniautorizador.model.Card;
import com.vr.mini.autorizador.miniautorizador.repository.CardRepository;
import com.vr.mini.autorizador.miniautorizador.util.RedisUtil;
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

  public String performTransaction(TransactionRequest request) {
    try {
      log.debug("Starting perform transaction for card: {}", request.getNumber());

      /* Decidi criar no redis uma chave com o número do cartão para garantir
      que não haja duas transações simultâneas para o mesmo cartão */
      redisUtil.set(request.getNumber(), "processing");

      Card card = getCardByRequest(request);

      validate(request, card);

      card.setBalance(card.getBalance().subtract(request.getValue()));
      cardRepository.save(card);

      log.debug("Transaction performed successfully for card with number {}", request.getNumber());

      return "OK";
    } finally {
      redisUtil.delete(request.getNumber());
    }
  }

  private void validate(TransactionRequest request, Card card) {
    if (!card.getPassword().equals(request.getPassword())) {
      log.error("Invalid password for card with number {}", request.getNumber());
      throw new InvalidPasswordException("SENHA_INVALIDA");
    }

    if (redisUtil.exists(request.getNumber())) {
      log.error("Transaction already in progress for card with number {}", request.getNumber());
      throw new TransactionInProgressException("TRANSACTION_ALREADY_IN_PROGRESS");
    }

    if (card.getBalance().compareTo(request.getValue()) < 0) {


      log.error("Insufficient balance for card: {}", request.getNumber());
      throw new InsufficientBalanceException("SALDO_INSUFICIENTE");
    }
  }

  private Card getCardByRequest(TransactionRequest request) {
    return cardRepository
        .findByNumber(request.getNumber())
        .orElseThrow(
            () -> {
              log.error("Card with number {} not found", request.getNumber());
              return new CardNotFoundException("CARTAO_INEXISTENTE");
            });
  }
}
