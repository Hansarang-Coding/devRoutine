package com.likelion.devroutine.likes.service;

import com.likelion.devroutine.certification.domain.Certification;
import com.likelion.devroutine.certification.repository.CertificationRepository;
import com.likelion.devroutine.likes.domain.Like;
import com.likelion.devroutine.likes.exception.CertificationNotFoundException;
import com.likelion.devroutine.likes.exception.LikeNotFoundException;
import com.likelion.devroutine.likes.repository.LikeRepository;
import com.likelion.devroutine.user.domain.User;
import com.likelion.devroutine.user.exception.UserNotFoundException;
import com.likelion.devroutine.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final CertificationRepository certificationRepository;

    @Transactional
    public String doLikes(Long certificationId, String oauthId) {
        if (isAlreadyPressedLike(certificationId, oauthId)) {
            deleteLikes(certificationId, oauthId);
        } else {
            Like like = Like.createLike(getUser(oauthId), getCertification(certificationId));
            likeRepository.save(like);
        }
        return "좋아요 생성 성공";
    }

    private boolean isAlreadyPressedLike(Long certificationId, String oauthId) {
        return likeRepository.existsByCertificationIdAndUserId(certificationId,
                getUser(oauthId).getId());
    }

    public Integer countLikes(Long certificationId) {
        Certification certification = getCertification(certificationId);
        return likeRepository.countByCertification(certification);
    }

    @Transactional
    public String deleteLikes(Long certificationId, String oauthId) {
        User user = getUser(oauthId);
        Certification certification = getCertification(certificationId);
        likeRepository.delete(getLike(user, certification));
        return "좋아요가 취소되었습니다";
    }

    private Like getLike(User user, Certification certification) {
        return likeRepository.findByCertificationAndUser(certification, user)
                .orElseThrow(LikeNotFoundException::new);
    }

    public User getUser(String oauthId) {
        return userRepository.findByOauthId(oauthId)
                .orElseThrow(UserNotFoundException::new);
    }

    public Certification getCertification(Long id) {
        return certificationRepository.findById(id)
                .orElseThrow(CertificationNotFoundException::new);
    }

}