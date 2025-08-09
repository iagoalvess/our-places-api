package br.com.ourplaces.our_places_api.repository;

import br.com.ourplaces.our_places_api.model.Place;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    @EntityGraph(attributePaths = {"ratings", "ratings.user"})
    List<Place> findAllByCoupleId(Long coupleId);
}
