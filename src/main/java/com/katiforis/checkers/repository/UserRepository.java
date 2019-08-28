package com.katiforis.checkers.repository;

import com.katiforis.checkers.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUserId(String userId);
    long deleteByUserId(String userId);
    User save(User user);
    Page<User> findAllByOrderByPlayerDetails_EloDesc(Pageable pageable);
    @Query("select count(u) from User u where u.playerDetails.elo > ?1")
    long countByPlayerDetailsElo(int elo);
}