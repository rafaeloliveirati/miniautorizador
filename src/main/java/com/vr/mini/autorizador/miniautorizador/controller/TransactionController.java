package com.vr.mini.autorizador.miniautorizador.controller;

import com.vr.mini.autorizador.miniautorizador.dto.request.TransactionRequest;
import com.vr.mini.autorizador.miniautorizador.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacoes")
@Tag(name = "Transaction API", description = "API for managing transactions")
public class TransactionController {

  private final TransactionService transactionService;

  @Autowired
  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @PostMapping
  @Operation(
      summary = "Perform a transaction",
      description = "Perform a transaction with the provided details")
  public ResponseEntity<String> performTransaction(
      @RequestBody TransactionRequest transactionRequest) {
    String result = transactionService.performTransaction(transactionRequest);
    return ResponseEntity.status(201).body(result);
  }
}
