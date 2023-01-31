package com.likelion.devroutine.participant.repository;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.participant.domain.Participant;
import com.likelion.devroutine.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findByUserAndChallenge(User user, Challenge challenge);
    List<Participant> findAllByChallenge(Challenge challenge);
    void deleteAllByChallenge(Challenge challenge);
}
