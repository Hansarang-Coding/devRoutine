package com.likelion.devroutine.invite.repository;

import com.likelion.devroutine.invite.domain.Invite;
import com.likelion.devroutine.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InviteRepository extends JpaRepository<Invite, Long>, InviteRepositoryCustom {
}
