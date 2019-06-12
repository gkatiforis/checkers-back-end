package com.katiforis.checkers.repository;

import com.katiforis.checkers.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUserId(String userId);
    User save(User user);
    Page<User> findAllByOrderByPlayerDetails_EloDesc(Pageable pageable);
}