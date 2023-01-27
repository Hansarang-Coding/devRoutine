package com.likelion.devroutine.comment.service;

import com.likelion.devroutine.auth.domain.User;
import com.likelion.devroutine.auth.repository.UserRepository;
import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.dto.ChallengeCreateRequest;
import com.likelion.devroutine.challenge.dto.ChallengeCreateResponse;
import com.likelion.devroutine.comment.domain.Certification;
import com.likelion.devroutine.comment.domain.Comment;
import com.likelion.devroutine.comment.dto.CommentCreateResponse;
import com.likelion.devroutine.comment.dto.CommentRequest;
import com.likelion.devroutine.comment.dto.CommentResponse;
import com.likelion.devroutine.comment.exception.UserNotFoundException;
import com.likelion.devroutine.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.cert.Certificate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentCreateResponse createComment(Long certificationId, CommentRequest request, String name) {
        //API Authentication

        //Certification certification = findCertification(CertificationId)

        User user = findUser(name);

        //id들
        Comment savedComment = commentRepository.save(Comment.createComment(request.getComment(), certificationId, user));
        return CommentCreateResponse.of(savedComment);
    }


    public Page<CommentResponse> findAll(Long certificationId, Pageable pageable) {
        //validateCertificationExists(postId);
        return CommentResponse.of(commentRepository.findAllByCertificationId(certificationId, pageable));
    }


//    private Certification findPost(Long certificationId) {
//        return CertificationRepository.findById(CertificationId).orElseThrow(() ->
//                new CertificationNotFoundException());
//    }

    private User findUser(String name) {
        return userRepository.findByName(name).orElseThrow(() ->
                new UserNotFoundException());
    }


//    private void validateCertificationExists(Long certificationId){
//        if(!Repository.existsById(certifitcationId)){
//            throw new CertificationNotFoundException()
//    }
//}




}
