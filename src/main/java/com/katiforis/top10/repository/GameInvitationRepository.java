package com.katiforis.top10.repository;

import com.katiforis.top10.model.GameInvitation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameInvitationRepository extends CrudRepository<GameInvitation, Long> {
    GameInvitation save(GameInvitation gameInvitation);
}