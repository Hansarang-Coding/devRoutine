package com.likelion.devroutine.challenge.service;

import com.likelion.devroutine.challenge.exception.InaccessibleChallengeException;
import com.likelion.devroutine.hashtag.dto.HashTagResponse;
import com.likelion.devroutine.invite.domain.Invite;
import com.likelion.devroutine.invite.repository.InviteRepository;
import com.likelion.devroutine.participant.domain.Participation;
import com.likelion.devroutine.participant.dto.ParticipationResponse;
import com.likelion.devroutine.participant.exception.DuplicatedParticipationException;
import com.likelion.devroutine.user.domain.User;
import com.likelion.devroutine.user.dto.UserResponse;
import com.likelion.devroutine.user.exception.UserNotFoundException;
import com.likelion.devroutine.user.repository.UserRepository;
import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.dto.*;
import com.likelion.devroutine.challenge.enumerate.ResponseMessage;
import com.likelion.devroutine.challenge.exception.ChallengeNotFoundException;
import com.likelion.devroutine.challenge.exception.InProgressingChallengeException;
import com.likelion.devroutine.challenge.exception.InvalidPermissionException;
import com.likelion.devroutine.challenge.repository.ChallengeRepository;
import com.likelion.devroutine.hashtag.domain.ChallengeHashTag;
import com.likelion.devroutine.hashtag.domain.HashTag;
import com.likelion.devroutine.hashtag.dto.ChallengeHashTagResponse;
import com.likelion.devroutine.hashtag.repository.ChallengeHashTagRepository;
import com.likelion.devroutine.hashtag.repository.HashTagRepository;
import com.likelion.devroutine.participant.repository.ParticipationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final HashTagRepository hashTagRepository;
    private final ChallengeHashTagRepository challengeHashTagRepository;
    private final UserRepository userRepository;
    private final ParticipationRepository participationRepository;
    private final InviteRepository inviteRepository;

    public ChallengeService(
            ChallengeRepository challengeRepository,
            HashTagRepository hashTagRepository,
            ChallengeHashTagRepository challengeHashTagRepository,
            UserRepository userRepository, ParticipationRepository participationRepository, InviteRepository inviteRepository) {
        this.challengeRepository = challengeRepository;
        this.hashTagRepository = hashTagRepository;
        this.challengeHashTagRepository = challengeHashTagRepository;
        this.userRepository = userRepository;
        this.participationRepository = participationRepository;
        this.inviteRepository = inviteRepository;
    }

    public List<ChallengeDto> findAllChallenge() {
        List<Challenge> challenges = challengeRepository.findAllSortById();
        return ChallengeDto.toList(challenges, getChallengeHashTagResponse(challenges));
    }

    public List<ChallengeDto> findAllChallengeTitle(String keyword) {
        List<Challenge> challenges = challengeRepository.findSearchTitleSortById(keyword);
        return ChallengeDto.toList(challenges, getChallengeHashTagResponse(challenges));
    }
    //무한스크롤
    /*public List<ChallengeDto> findAllChallenge(Long challengeId, int size) {
        List<Challenge> challenges = challengeRepository.findAllSortById(challengeId, PageRequest.of(0, size));
        return ChallengeDto.toList(challenges, getChallengeHashTagResponse(challenges));
    }

    public List<ChallengeDto> findAllChallengeTitle(Long challengeId, int size, String keyword) {
        List<Challenge> challenges = challengeRepository.findSearchTitleSortById(challengeId, keyword, PageRequest.of(0, size));
        return ChallengeDto.toList(challenges, getChallengeHashTagResponse(challenges));
    }*/

    public ChallengeDto findByChallengeId(Long challengeId) {
        Challenge challenge = getChallenge(challengeId);
        if(!challenge.getVigibility()) throw new InaccessibleChallengeException();
        return ChallengeDto.toDto(challenge, ChallengeHashTagResponse.of(challenge.getChallengeHashTags()));
    }
    public ChallengeDto findByChallengeId(Long challengeId, String oauthId){
        Challenge challenge = getChallenge(challengeId);
        isViewable(challenge, oauthId);
        return ChallengeDto.toDto(challenge, ChallengeHashTagResponse.of(challenge.getChallengeHashTags()));
    }

    @Transactional
    public ChallengeCreateResponse createChallenge(String oauthId, ChallengeCreateRequest dto) {
        log.info(oauthId);
        User user = getUser(oauthId);
        Challenge savedChallenge = challengeRepository.save(Challenge.createChallenge(user.getId(), dto));
        List<String> hashTags = extractHashTag(dto.getHashTag());
        saveNewHashTags(savedChallenge.getId(), hashTags);
        participationRepository.save(Participation.createParticipant(user, savedChallenge));
        return ChallengeCreateResponse.toResponse(savedChallenge, hashTags);
    }

    @Transactional
    public ChallengeResponse deleteChallenge(String oauthId, Long id) {
        User user = getUser(oauthId);
        Challenge challenge = getChallenge(id);

        matchWriterAndUser(challenge, user);

        isProgressChallenge(challenge.getStartDate());
        participationRepository.deleteAllByChallenge(challenge);
        challenge.deleteChallenge();
        return ChallengeResponse.builder()
                .message(ResponseMessage.CHALLENGE_DELETE_SUCCESS.getMessage()).build();
    }

    @Transactional
    public ChallengeResponse modifyChallenge(String oauthId, Long challengeId, ChallengeModifiyRequest dto) {
        User user = getUser(oauthId);
        Challenge challenge = getChallenge(challengeId);
        matchWriterAndUser(challenge, user);
        isProgressChallenge(challenge.getStartDate());
        challenge.updateChallenge(dto);
        removeChallengeHashTag(challenge);
        saveNewHashTags(challengeId, extractHashTag(dto.getHashtag()));
        return ChallengeResponse.builder()
                .message(ResponseMessage.CHALLENGE_MODIFY_SUCCESS.getMessage()).build();
    }

    @Transactional
    public ParticipationResponse participateChallenge(String oauthId, Long challengeId) {
        User user=getUser(oauthId);
        Challenge challenge=getChallenge(challengeId);
        validateParticipate(user, challenge);
        Participation savedParticipation = participationRepository.save(Participation.createParticipant(user, challenge));
        return ParticipationResponse.builder()
                .challengeId(savedParticipation.getChallenge().getId())
                .message(com.likelion.devroutine.participant.enumerate.ResponseMessage.PARTICIPATE_SUCCESS.getMessage())
                .build();
    }

    private void removeChallengeHashTag(Challenge challenge) {
        List<ChallengeHashTag> challengeHashTags = challenge.getChallengeHashTags();
        challengeHashTagRepository.deleteAll(challengeHashTags);
        challengeHashTags.clear();
    }

    private void saveNewHashTags(Long challengeId, List<String> hashTags) {
        Challenge challenge = getChallenge(challengeId);
        for (String hashTagContents : hashTags) { //#자바 #김영한
            HashTag savedHashTag = hashTagRepository.findByContents(hashTagContents)
                    .orElseGet(() -> hashTagRepository.save(HashTag.createHashTag(hashTagContents)));
            challengeHashTagRepository.save(ChallengeHashTag.create(challenge, savedHashTag));
        }
    }

    private Map<Long, List<ChallengeHashTagResponse>> getChallengeHashTagResponse(List<Challenge> challenges){
        return challenges
                .stream()
                .collect(Collectors.toMap(
                        challenge-> challenge.getId(),
                        challenge->ChallengeHashTagResponse.of(getHashTags(challenge.getId()))
                ));
    }

    private List<ChallengeHashTag> getHashTags(Long challengeId) {
        return challengeHashTagRepository.findByChallengeId(challengeId);
    }

    public List<Challenge> findAll() {
        return challengeRepository.findAll();
    }

    private List<String> extractHashTag(String hashTag) {
        return Arrays.stream(hashTag.split("#"))
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());
    }

    public Challenge getChallenge(Long id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new ChallengeNotFoundException());
        return challenge;
    }

    public boolean isProgressChallenge(LocalDate startDate) {
        if (LocalDate.now().isAfter(startDate))
            throw new InProgressingChallengeException();
        return false;
    }

    public User getUser(String oauthId) {
        User user = userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new UserNotFoundException());
        return user;
    }

    public boolean matchWriterAndUser(Challenge challenge, User user) {
        if (!user.getId().equals(challenge.getUserId())) {
            throw new InvalidPermissionException();
        }
        return true;
    }
    public boolean isParticipate(Long challengeId, String oauthId) {
        if (participationRepository.findByUserAndChallenge(getUser(oauthId), getChallenge(challengeId)).isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean isViewable(Challenge challenge, String oauthId) {
        //공개 챌린지이거나 초대받은 경우
        if(challenge.getVigibility() || isPresentInvite(challenge.getId(), oauthId) || isParticipate(challenge.getId(), oauthId)){
            return true;
        }
        throw new InaccessibleChallengeException();
    }

    private boolean isPresentInvite(Long challengeId, String oauthId) {
        if(oauthId!=null && !inviteRepository.findAllByChallengeIdAndInviteeId(challengeId, getUser(oauthId).getId()).isEmpty()){
            return true;
        }
        return false;
    }

    public boolean validateDuplicateParticipate(User user, Challenge challenge) {
        participationRepository.findByUserAndChallenge(user, challenge)
                .ifPresent(participant -> {
                    throw new DuplicatedParticipationException();
                });
        return true;
    }

    public boolean validateParticipate(User user, Challenge challenge){
        isViewable(challenge, user.getOauthId());
        isProgressChallenge(challenge.getStartDate());
        validateDuplicateParticipate(user, challenge);
        return true;
    }

    public List<HashTagResponse> getRandomHashTag(){
        List<HashTag> hashTags = hashTagRepository.findHashTagsByRandom();
        return HashTagResponse.of(hashTags);
    }

    public UserResponse getUserResponse(String oauthId) {
        User user=getUser(oauthId);
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
