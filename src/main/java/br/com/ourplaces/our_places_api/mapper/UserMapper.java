package br.com.ourplaces.our_places_api.mapper;

import br.com.ourplaces.our_places_api.dto.UserCreateDTO;
import br.com.ourplaces.our_places_api.dto.UserViewDTO;
import br.com.ourplaces.our_places_api.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "accountNonExpired", constant = "true")
    @Mapping(target = "accountNonLocked", constant = "true")
    @Mapping(target = "credentialsNonExpired", constant = "true")
    @Mapping(target = "authorities", ignore = true)
    User toModel(UserCreateDTO dto);

    UserViewDTO toViewDTO(User model);
}
