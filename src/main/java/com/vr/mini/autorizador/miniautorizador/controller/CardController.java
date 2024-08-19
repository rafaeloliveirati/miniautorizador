package com.vr.mini.autorizador.miniautorizador.controller;

import com.vr.mini.autorizador.miniautorizador.dto.request.CardRequestDTO;
import com.vr.mini.autorizador.miniautorizador.dto.response.CardResponseDTO;
import com.vr.mini.autorizador.miniautorizador.mapper.CardMapper;
import com.vr.mini.autorizador.miniautorizador.model.Card;
import com.vr.mini.autorizador.miniautorizador.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cartoes")
@Tag(name = "Card API", description = "API for managing cards")
public class CardController {

  private final CardService cardService;

  @Autowired
  public CardController(CardService cardService) {
    this.cardService = cardService;
  }

  @PostMapping
  @Operation(
      summary = "Create a new card",
      description = "Create a new card with the provided details")
  public ResponseEntity<CardResponseDTO> create(@Valid @RequestBody CardRequestDTO request) {
    Card card = cardService.create(request);
    CardResponseDTO response = CardMapper.INSTANCE.toResponse(card);
    return ResponseEntity.status(201).body(response);
  }

  @GetMapping("/{number}")
  @Operation(
      summary = "Get card balance",
      description = "Retrieve the balance of a card by its number")
  public ResponseEntity<BigDecimal> getBalance(@PathVariable String number) {
    BigDecimal balance = cardService.getBalance(number);
    return ResponseEntity.ok(balance);
  }
}
