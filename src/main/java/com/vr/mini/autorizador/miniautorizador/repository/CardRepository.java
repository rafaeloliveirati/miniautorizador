package com.vr.mini.autorizador.miniautorizador.repository;

import com.vr.mini.autorizador.miniautorizador.model.Card;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CardRepository extends MongoRepository<Card, String> {
  Optional<Card> findByNumber(String number);
}
