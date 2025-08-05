package br.com.ourplaces.our_places_api.service;

import br.com.ourplaces.our_places_api.dto.CoupleViewDTO;
import br.com.ourplaces.our_places_api.dto.UserCreateDTO;
import br.com.ourplaces.our_places_api.mapper.CoupleMapper;
import br.com.ourplaces.our_places_api.model.Couple;
import br.com.ourplaces.our_places_api.model.User;
import br.com.ourplaces.our_places_api.repository.CoupleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CoupleService {
    private final UserService userService;
    private final CoupleRepository coupleRepository;
    private final CoupleMapper coupleMapper;

    public CoupleService(CoupleRepository coupleRepository, UserService userService, CoupleMapper coupleMapper) {
        this.coupleRepository = coupleRepository;
        this.userService = userService;
        this.coupleMapper = coupleMapper;
    }

    @Transactional
    public CoupleViewDTO createCouple(UserCreateDTO user1DTO) {
        User user1 = userService.register(user1DTO);

        Couple couple = new Couple();
        couple.setUser1(user1);
        couple.setInviteCode(UUID.randomUUID().toString());

        Couple savedCouple = coupleRepository.save(couple);
        return coupleMapper.toViewDTO(savedCouple);
    }

    @Transactional
    public CoupleViewDTO joinCouple(String inviteCode, UserCreateDTO user2DTO) {
        Couple couple = coupleRepository.findByInviteCode(inviteCode).orElseThrow(() -> new IllegalArgumentException("Invite code not found or already in use"));

        if (couple.getUser2() != null) {
            throw new IllegalArgumentException("This couple is already complete.");
        }

        User user2 = userService.register(user2DTO);
        couple.setUser2(user2);
        couple.setInviteCode(inviteCode);

        Couple updatedCouple = coupleRepository.save(couple);
        return coupleMapper.toViewDTO(updatedCouple);
    }
}
