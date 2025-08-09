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
 
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class CoupleService {
    private final UserService userService;
    private final CurrentUserService currentUserService;
    private final CoupleRepository coupleRepository;
    private final CoupleMapper coupleMapper;

    public CoupleService(CoupleRepository coupleRepository, UserService userService, CoupleMapper coupleMapper, CurrentUserService currentUserService) {
        this.coupleRepository = coupleRepository;
        this.userService = userService;
        this.coupleMapper = coupleMapper;
        this.currentUserService = currentUserService;
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
        Couple couple = getAuthenticatedCouple();

        ImportantDate newDate = ImportantDateMapper.toModel(dateDTO);
        couple.getImportantDates().add(newDate);

        Couple updatedCouple = coupleRepository.save(couple);
        return coupleMapper.toViewDTO(updatedCouple);
    }

    @Transactional
    public List<ImportantDateDTO> getImportantDates() {
        Couple couple = getAuthenticatedCouple();

        return couple.getImportantDates().stream()
                .map(ImportantDateMapper::toDTO)
                .toList();
    }

    @Transactional
    public void removeImportantDate(String description, java.time.LocalDate date) {
        Couple couple = getAuthenticatedCouple();

        boolean removed = couple.getImportantDates()
                .removeIf(d -> d.getDescription().equals(description) && d.getDate().equals(date));

        if (!removed) {
            throw new ResourceNotFoundException("Important date not found.");
        }

        coupleRepository.save(couple);
    }

    @Transactional
    public CoupleViewDTO getCouple() {
        Couple couple = getAuthenticatedCouple();

        return coupleMapper.toViewDTO(couple);
    }

    @Transactional
    public void updateCouplePicture(MultipartFile file) {
        if (file.isEmpty()) throw new IllegalArgumentException("File not sent.");
        if (file.getSize() > 5 * 1024 * 1024) throw new IllegalArgumentException("File too large. Maximum 5MB.");
        if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) throw new IllegalArgumentException("Invalid file type. Please upload an image.");

        Couple couple = getAuthenticatedCouple();

        try {
            couple.setCouplePicture(file.getBytes());
            couple.setPictureMediaType(file.getContentType());
        } catch (IOException e) {
            throw new RuntimeException("Error processing file.", e);
        }
        coupleRepository.save(couple);
    }


    @Transactional
    public Map<String, Object> getCouplePicture() {
        Couple couple = getAuthenticatedCouple();

        if (couple.getCouplePicture() == null) {
            throw new ResourceNotFoundException("Couple does not have a profile picture.");
        }

        return Map.of("imageBytes", couple.getCouplePicture(), "mediaType", couple.getPictureMediaType());
    }

    private Couple getAuthenticatedCouple() {
        User currentUser = currentUserService.getAuthenticatedUser();
        return coupleRepository.findByUser1IdOrUser2Id(currentUser.getId(), currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Couple not found for the authenticated user."));
    }
}
