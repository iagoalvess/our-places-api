package br.com.ourplaces.our_places_api.repository;

import br.com.ourplaces.our_places_api.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    boolean existsByPlaceIdAndUserId(Long placeId, Long userId);
}
