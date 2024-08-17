package com.vr.mini.autorizador.miniautorizador.model;

import java.math.BigDecimal;
import java.util.UUID;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cards")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Card {
  @NotNull @Id private String id = UUID.randomUUID().toString();

  @NotNull private String number;

  @NotNull private BigDecimal balance;

  @NotNull private String password;
}
