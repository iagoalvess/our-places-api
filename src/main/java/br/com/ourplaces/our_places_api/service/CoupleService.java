package br.com.ourplaces.our_places_api.service;

import br.com.ourplaces.our_places_api.dto.CoupleViewDTO;
import br.com.ourplaces.our_places_api.dto.UserCreateDTO;
import br.com.ourplaces.our_places_api.mapper.CoupleMapper;
import br.com.ourplaces.our_places_api.model.CoupleModel;
import br.com.ourplaces.our_places_api.model.UserModel;
import br.com.ourplaces.our_places_api.repository.CoupleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CoupleService {
    private UserService userService;
    private CoupleRepository coupleRepository;
    private CoupleMapper coupleMapper;

    public CoupleService(CoupleRepository coupleRepository, UserService userService, CoupleMapper coupleMapper) {
        this.coupleRepository = coupleRepository;
        this.userService = userService;
        this.coupleMapper = coupleMapper;
    }

    @Transactional
    public CoupleViewDTO joinCouple(String inviteCode, UserCreateDTO user2DTO) {
        CoupleModel couple = coupleRepository.findByInviteCode(inviteCode).orElseThrow(() -> new IllegalArgumentException("Invite code not found or already in use"));

        if (couple.getUser2() != null) {
            throw new IllegalArgumentException("This couple is already complete.");
        }

        UserModel user2 = userService.register(user2DTO);
        couple.setUser2(user2);
        couple.setInviteCode(inviteCode);

        CoupleModel updatedCouple = coupleRepository.save(couple);
        return coupleMapper.toViewDTO(updatedCouple);
    }
}
