package br.com.ourplaces.our_places_api.mapper;

import br.com.ourplaces.our_places_api.dto.ImportantDateDTO;
import br.com.ourplaces.our_places_api.model.ImportantDate;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface ImportantDateMapper {
    ImportantDate toModel(ImportantDateDTO dto);

    ImportantDateDTO toDTO(ImportantDate model);

    Set<ImportantDateDTO> toDTOSet(Set<ImportantDate> models);
}
