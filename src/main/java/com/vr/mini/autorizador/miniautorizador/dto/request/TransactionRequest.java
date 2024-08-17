package com.vr.mini.autorizador.miniautorizador.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequest {

  @JsonProperty("numeroCartao")
  private String number;

  @JsonProperty("senhaCartao")
  private String password;

  @JsonProperty("valor")
  private BigDecimal value;
}
