package br.com.ourplaces.our_places_api.dto;

import java.util.List;

public record CoupleViewDTO(Long id, UserViewDTO user1, UserViewDTO user2,
                            String inviteCode,
                            List<ImportantDateDTO> importantDates,
                            String couplePictureUrl) {
}
