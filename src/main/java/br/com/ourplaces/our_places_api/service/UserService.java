package br.com.ourplaces.our_places_api.service;

import br.com.ourplaces.our_places_api.dto.UserCreateDTO;
import br.com.ourplaces.our_places_api.exception.EmailAlreadyExistsException;
import br.com.ourplaces.our_places_api.mapper.UserMapper;
import br.com.ourplaces.our_places_api.model.User;
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
    public User register(UserCreateDTO userCreateDTO) {
        userRepository.findByEmail(userCreateDTO.email()).ifPresent(user -> {
            throw new EmailAlreadyExistsException("Email already exists");
        });

        User user = userMapper.toModel(userCreateDTO);
        user.setPassword(passwordEncoder.encode(userCreateDTO.password()));

        return userRepository.save(user);
    }
}
