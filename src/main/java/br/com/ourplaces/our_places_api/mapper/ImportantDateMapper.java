package br.com.ourplaces.our_places_api.mapper;

import br.com.ourplaces.our_places_api.dto.ImportantDateDTO;
import br.com.ourplaces.our_places_api.model.ImportantDate;

public final class ImportantDateMapper {
    private ImportantDateMapper() {}

    public static ImportantDate toModel(ImportantDateDTO dto) {
        if (dto == null) return null;
        return new ImportantDate(dto.description(), dto.date());
    }

    public static ImportantDateDTO toDTO(ImportantDate model) {
        if (model == null) return null;
        return new ImportantDateDTO(model.getDescription(), model.getDate());
    }
}
