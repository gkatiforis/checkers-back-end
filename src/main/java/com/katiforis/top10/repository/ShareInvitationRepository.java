package com.katiforis.top10.repository;

import com.katiforis.top10.model.ShareInvitation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShareInvitationRepository extends CrudRepository<ShareInvitation, Long> {
    ShareInvitation save(ShareInvitation shareInvitation);
}