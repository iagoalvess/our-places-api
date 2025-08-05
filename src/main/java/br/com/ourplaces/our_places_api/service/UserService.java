package br.com.ourplaces.our_places_api.service;

import br.com.ourplaces.our_places_api.dto.UserCreateDTO;
import br.com.ourplaces.our_places_api.exception.EmailAlreadyExistsException;
import br.com.ourplaces.our_places_api.mapper.UserMapper;
import br.com.ourplaces.our_places_api.model.UserModel;
import br.com.ourplaces.our_places_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserModel register(UserCreateDTO userCreateDTO) {
        userRepository.findByEmail(userCreateDTO.email()).ifPresent(user -> {
            throw new EmailAlreadyExistsException("Email already exists");
        });

        UserModel userModel = userMapper.toModel(userCreateDTO);
        userModel.setPassword(passwordEncoder.encode(userCreateDTO.password()));

        return userRepository.save(userModel);
    }
}
