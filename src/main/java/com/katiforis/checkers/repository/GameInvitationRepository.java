package com.katiforis.checkers.repository;

import com.katiforis.checkers.model.GameInvitation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameInvitationRepository extends CrudRepository<GameInvitation, Long> {
    GameInvitation save(GameInvitation gameInvitation);
}