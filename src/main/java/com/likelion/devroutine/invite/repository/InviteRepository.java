package com.likelion.devroutine.invite.repository;

import com.likelion.devroutine.invite.domain.Invite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InviteRepository extends JpaRepository<Invite, Long> {
}
