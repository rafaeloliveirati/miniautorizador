package com.vr.mini.autorizador.miniautorizador.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardResponseDTO implements Serializable {
  @Serial private static final long serialVersionUID = 1L;

  @JsonProperty("numeroCartao")
  private String number;

  @JsonProperty("senha")
  private String password;
}
