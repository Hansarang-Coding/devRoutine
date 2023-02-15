package com.likelion.devroutine.invite.repository;

import com.likelion.devroutine.invite.domain.Invite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InviteRepository extends JpaRepository<Invite, Long>, InviteRepositoryCustom {
    Optional<Invite> findByChallengeIdAndInviteeId(Long challengeId, Long inviteeId);
}
