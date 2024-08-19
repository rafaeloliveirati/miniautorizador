package com.vr.mini.autorizador.miniautorizador.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequestDTO {

  @NotEmpty
  @JsonProperty("numeroCartao")
  private String number;

  @NotEmpty
  @JsonProperty("senhaCartao")
  private String password;

  @NotNull
  @JsonProperty("valor")
  private BigDecimal value;
}
