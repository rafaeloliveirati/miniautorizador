package com.vr.mini.autorizador.miniautorizador.mapper;

import com.vr.mini.autorizador.miniautorizador.dto.request.CardRequestDTO;
import com.vr.mini.autorizador.miniautorizador.dto.response.CardResponseDTO;
import com.vr.mini.autorizador.miniautorizador.model.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CardMapper {
  CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "balance", ignore = true)
  Card toEntity(CardRequestDTO request);

  CardResponseDTO toResponse(Card card);
}
