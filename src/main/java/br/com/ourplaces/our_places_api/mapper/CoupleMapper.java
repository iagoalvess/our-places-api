package br.com.ourplaces.our_places_api.mapper;

import br.com.ourplaces.our_places_api.dto.CoupleViewDTO;
import br.com.ourplaces.our_places_api.model.Couple;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ImportantDateMapper.class})
public interface CoupleMapper {
    @Mapping(target = "couplePictureUrl", expression = "java(generatePictureUrl(model))")
    CoupleViewDTO toViewDTO(Couple model);

    default String generatePictureUrl(Couple model) {
        if (model != null && model.getCouplePicture() != null && model.getCouplePicture().length > 0) {
            return "/couples/picture";
        }
        return null;
    }
}
