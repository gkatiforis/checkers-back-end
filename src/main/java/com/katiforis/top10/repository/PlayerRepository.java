package com.katiforis.top10.repository;

import com.katiforis.top10.model.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {
    Player findByPlayerId(String id);
    Player save(Player player);
}