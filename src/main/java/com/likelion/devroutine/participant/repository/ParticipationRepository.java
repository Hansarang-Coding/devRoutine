package com.likelion.devroutine.participant.repository;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.participant.domain.Participation;
import com.likelion.devroutine.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long>, ParticipationRepositoryCustom {
    Optional<Participation> findByUserAndChallenge(User user, Challenge challenge);
    List<Participation> findAllByChallenge(Challenge challenge);
    void deleteAllByChallenge(Challenge challenge);

    @Query("select p from Participation p join fetch p.challenge where p.user.id =:userId")
    List<Participation> findAllByUserId(@Param("userId") Long userId);
}
