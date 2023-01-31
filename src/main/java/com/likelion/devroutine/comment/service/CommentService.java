package com.likelion.devroutine.comment.service;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.dto.ChallengeCreateRequest;
import com.likelion.devroutine.challenge.dto.ChallengeCreateResponse;
import com.likelion.devroutine.challenge.enumerate.ResponseMessage;
import com.likelion.devroutine.comment.domain.Comment;
import com.likelion.devroutine.comment.dto.*;
import com.likelion.devroutine.comment.exception.CommentNotFoundException;
import com.likelion.devroutine.comment.exception.UserNotFoundException;
import com.likelion.devroutine.comment.exception.UserUnauthorizedException;
import com.likelion.devroutine.comment.repository.CommentRepository;
import com.likelion.devroutine.user.domain.User;
import com.likelion.devroutine.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.cert.Certificate;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentCreateResponse createComment(Long certificationId, CommentRequest request, String name) {
        //Certification certification = findCertification(CertificationId)
        User user = getUser(name);

        //id들
        Comment savedComment = commentRepository.save(Comment.createComment(request.getComment(), certificationId, user));
        return CommentCreateResponse.of(savedComment);
    }


    public Page<CommentResponse> findAll(Long certificationId, Pageable pageable) {
        //validateCertificationExists(CertificationId);
        return CommentResponse.of(commentRepository.findAllByCertificationId(certificationId, pageable));
    }

    @Transactional
    public CommentDeleteResponse deleteComment(Long certificationId, Long commentId, String name) {
        //validateCertificationExists(certificationId);
        Comment comment = getCommentByAuthorizedUser(commentId, name);
        comment.deleteComment();
        return CommentDeleteResponse.of(ResponseMessage.CHALLENGE_DELETE_SUCCESS.getMessage(), commentId);
    }

    @Transactional
    public CommentUpdateResponse updateComment(Long certificationId, Long commentId, CommentRequest request, String name) {
        //validateCertificationExists(certificationId);
        Comment comment = getCommentByAuthorizedUser(commentId, name);
        comment.updateComment(request.getComment());
        return CommentUpdateResponse.of(comment);
    }


//    private Certification findCertification(Long certificationId) {
//        return CertificationRepository.findById(CertificationId).orElseThrow(() ->
//                new CertificationNotFoundException());
//    }



    private User getUser(String name) {
        return userRepository.findByName(name).orElseThrow(() ->
                new UserNotFoundException());
    }


    //    private void validateCertificationExists(Long certificationId){
//        if(!Repository.existsById(certificationId)){
//            throw new CertificationNotFoundException()
//    }
//}
    private Comment getCommentByAuthorizedUser(Long commentId, String userName) {
        User findUser = getUser(userName);
        Comment comment = getComment(commentId);
        if (Objects.equals(comment.getUser().getId(), findUser.getId()))
            return comment;
        else throw new UserUnauthorizedException();
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new CommentNotFoundException());
    }


}
