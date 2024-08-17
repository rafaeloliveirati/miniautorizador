package com.vr.mini.autorizador.miniautorizador.dto.response;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardResponse {

  @NotNull(message = "Card number cannot be null")
  @JsonProperty("numeroCartao")
  private String number;

  @NotNull(message = "Card password cannot be null")
  @JsonProperty("senha")
  private String password;
}
