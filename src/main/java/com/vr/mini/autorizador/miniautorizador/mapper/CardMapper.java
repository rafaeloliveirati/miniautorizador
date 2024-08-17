package com.vr.mini.autorizador.miniautorizador.mapper;

import com.vr.mini.autorizador.miniautorizador.dto.request.CardRequest;
import com.vr.mini.autorizador.miniautorizador.dto.response.CardResponse;
import com.vr.mini.autorizador.miniautorizador.model.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CardMapper {
  CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

  @Mapping(target = "id", ignore = true)
  Card toEntity(CardRequest cardRequest);

  CardResponse toResponse(Card card);
}
