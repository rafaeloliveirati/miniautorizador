package com.vr.mini.autorizador.miniautorizador.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardRequestDTO {

  @NotEmpty
  @JsonProperty("numeroCartao")
  private String number;

  @NotEmpty
  @JsonProperty("senha")
  private String password;
}
