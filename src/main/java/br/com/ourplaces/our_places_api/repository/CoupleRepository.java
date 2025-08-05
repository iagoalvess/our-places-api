package br.com.ourplaces.our_places_api.repository;

import br.com.ourplaces.our_places_api.model.Couple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoupleRepository extends JpaRepository<Couple, Integer> {
    Optional<Couple> findByInviteCode(String inviteCode);

    Optional<Couple> findByUser1IdOrUser2Id(Long user1Id, Long user2Id);
}
