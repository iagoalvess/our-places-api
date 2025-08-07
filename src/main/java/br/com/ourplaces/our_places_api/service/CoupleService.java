package br.com.ourplaces.our_places_api.service;

import br.com.ourplaces.our_places_api.dto.CoupleViewDTO;
import br.com.ourplaces.our_places_api.dto.ImportantDateDTO;
import br.com.ourplaces.our_places_api.dto.UserCreateDTO;
import br.com.ourplaces.our_places_api.exception.ResourceNotFoundException;
import br.com.ourplaces.our_places_api.mapper.CoupleMapper;
import br.com.ourplaces.our_places_api.mapper.ImportantDateMapper;
import br.com.ourplaces.our_places_api.model.Couple;
import br.com.ourplaces.our_places_api.model.ImportantDate;
import br.com.ourplaces.our_places_api.model.User;
import br.com.ourplaces.our_places_api.repository.CoupleRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class CoupleService {
    private final UserService userService;
    private final CoupleRepository coupleRepository;
    private final CoupleMapper coupleMapper;
    private final ImportantDateMapper importantDateMapper;

    public CoupleService(CoupleRepository coupleRepository, UserService userService, CoupleMapper coupleMapper, ImportantDateMapper importantDateMapper) {
        this.coupleRepository = coupleRepository;
        this.userService = userService;
        this.coupleMapper = coupleMapper;
        this.importantDateMapper = importantDateMapper;
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

    @Transactional
    public CoupleViewDTO addImportantDate(ImportantDateDTO dateDTO) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Couple couple = coupleRepository.findByUser1IdOrUser2Id(currentUser.getId(), currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("Couple not found for the authenticated user."));

        ImportantDate newDate = importantDateMapper.toModel(dateDTO);
        couple.getImportantDates().add(newDate);

        Couple updatedCouple = coupleRepository.save(couple);
        return coupleMapper.toViewDTO(updatedCouple);
    }

    @Transactional
    public Set<ImportantDateDTO> getImportantDates() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Couple couple = coupleRepository.findByUser1IdOrUser2Id(currentUser.getId(), currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("Couple not found for the authenticated user."));

        return importantDateMapper.toDTOSet(couple.getImportantDates());
    }

    @Transactional
    public CoupleViewDTO getCouple() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Couple couple = coupleRepository.findByUser1IdOrUser2Id(currentUser.getId(), currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("Couple not found for the authenticated user."));

        return coupleMapper.toViewDTO(couple);
    }

    @Transactional
    public void updateCouplePicture(MultipartFile file) throws IOException {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Couple couple = coupleRepository.findByUser1IdOrUser2Id(currentUser.getId(), currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("Couple not found for the authenticated user."));

        couple.setCouplePicture(file.getBytes());
        couple.setPictureMediaType(file.getContentType());
        coupleRepository.save(couple);
    }

    @Transactional
    public Map<String, Object> getCouplePicture() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Couple couple = coupleRepository.findByUser1IdOrUser2Id(currentUser.getId(), currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("Couple not found for the authenticated user."));

        if (couple.getCouplePicture() == null) {
            throw new ResourceNotFoundException("Couple does not have a profile picture.");
        }

        return Map.of("imageBytes", couple.getCouplePicture(), "mediaType", couple.getPictureMediaType());
    }
}
