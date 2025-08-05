package br.com.ourplaces.our_places_api.mapper;

import br.com.ourplaces.our_places_api.dto.CoupleViewDTO;
import br.com.ourplaces.our_places_api.model.Couple;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CoupleMapper {
    CoupleViewDTO toViewDTO(Couple model);
}
