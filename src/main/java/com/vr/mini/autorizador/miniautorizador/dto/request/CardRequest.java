package com.vr.mini.autorizador.miniautorizador.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Builder
public class CardRequest {
  @NotBlank(message = "Number is mandatory")
  @JsonProperty("numeroCartao")
  private String number;

  @NotBlank(message = "Password is mandatory")
  @Size(min = 6, message = "Password must be at least 6 characters long")
  @JsonProperty("senha")
  private String password;

  @NotNull(message = "Balance is mandatory")
  @JsonProperty("saldo")
  private BigDecimal balance;
}
