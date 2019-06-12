package com.katiforis.checkers.repository;

import com.katiforis.checkers.model.ShareInvitation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShareInvitationRepository extends CrudRepository<ShareInvitation, Long> {
    ShareInvitation save(ShareInvitation shareInvitation);
}