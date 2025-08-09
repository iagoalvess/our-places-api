package br.com.ourplaces.our_places_api.mapper;

import br.com.ourplaces.our_places_api.dto.UserCreateDTO;
import br.com.ourplaces.our_places_api.dto.UserViewDTO;
import br.com.ourplaces.our_places_api.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toModel(UserCreateDTO dto) {
        if (dto == null) return null;
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        return user;
    }

    public UserViewDTO toViewDTO(User model) {
        if (model == null) return null;
        return new UserViewDTO(model.getId(), model.getName(), model.getEmail());
    }
}
