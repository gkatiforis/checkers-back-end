package com.katiforis.top10.repository;

import com.katiforis.top10.model.FriendInvitation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendInvitationRepository extends CrudRepository<FriendInvitation, Long> {
    FriendInvitation save(FriendInvitation friendInvitation);
}