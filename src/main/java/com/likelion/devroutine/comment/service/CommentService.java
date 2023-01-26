package com.likelion.devroutine.comment.service;

import com.likelion.devroutine.auth.domain.User;
import com.likelion.devroutine.auth.repository.UserRepository;
import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.dto.ChallengeCreateRequest;
import com.likelion.devroutine.challenge.dto.ChallengeCreateResponse;
import com.likelion.devroutine.comment.domain.Comment;
import com.likelion.devroutine.comment.dto.CommentCreateResponse;
import com.likelion.devroutine.comment.dto.CommentRequest;
import com.likelion.devroutine.comment.exception.UserNotFoundException;
import com.likelion.devroutine.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentCreateResponse createComment(Long certificationId, CommentRequest request, Long userId) {
        //API Authentication

        //Certification certification = findCertification(CertificationId);

        User user = findUser(7L);

        //id들
        Comment savedComment=commentRepository.save(Comment.createComment(request.getComment(),7L, user));
        return CommentCreateResponse.of(savedComment);
    }


//    private Certification findPost(Long certificationId) {
//        return CertificationRepository.findById(CertificationId).orElseThrow(() ->
//                new CertificationNotFoundException());
//    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException());
    }




}
