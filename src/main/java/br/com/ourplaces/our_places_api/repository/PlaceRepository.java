package br.com.ourplaces.our_places_api.repository;

import br.com.ourplaces.our_places_api.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
}
