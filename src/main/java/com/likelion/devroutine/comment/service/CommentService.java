package com.likelion.devroutine.comment.service;

import com.likelion.devroutine.certification.domain.Certification;
import com.likelion.devroutine.certification.repository.CertificationRepository;
import com.likelion.devroutine.challenge.enumerate.ResponseMessage;
import com.likelion.devroutine.comment.domain.Comment;
import com.likelion.devroutine.comment.dto.*;
import com.likelion.devroutine.comment.exception.CertificationNotFoundException;
import com.likelion.devroutine.comment.exception.CommentNotFoundException;
import com.likelion.devroutine.comment.exception.UserUnauthorizedException;
import com.likelion.devroutine.comment.repository.CommentRepository;
import com.likelion.devroutine.user.domain.User;
import com.likelion.devroutine.user.exception.UserNotFoundException;
import com.likelion.devroutine.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CertificationRepository certificationRepository;

    @Transactional
    public CommentCreateResponse createComment(Long certificationId, CommentRequest request, String oauthId) {
        Certification certification = findCertification(certificationId);
        User user = getUser(oauthId);
        Comment savedComment = commentRepository.save(Comment.createComment(request.getComment(), certification, user));
        return CommentCreateResponse.of(savedComment);
    }

    public Page<CommentResponse> findAll(Long certificationId, Pageable pageable) {
        validateCertificationExists(certificationId);
        return CommentResponse.of(commentRepository.findAllByCertificationId(certificationId, pageable));
    }

    @Transactional
    public CommentDeleteResponse deleteComment(Long certificationId, Long commentId, String oauthId) {
        validateCertificationExists(certificationId);
        Comment comment = getCommentByAuthorizedUser(commentId, oauthId);
        comment.deleteComment();
        return CommentDeleteResponse.of(ResponseMessage.CHALLENGE_DELETE_SUCCESS.getMessage(), commentId);
    }

    @Transactional
    public CommentUpdateResponse updateComment(Long certificationId, Long commentId, CommentRequest request, String oauthId) {
        validateCertificationExists(certificationId);
        Comment comment = getCommentByAuthorizedUser(commentId, oauthId);
        comment.updateComment(request.getComment());
        return CommentUpdateResponse.of(comment);
    }

    private Certification findCertification(Long certificationId) {
        return certificationRepository.findById(certificationId)
                .orElseThrow(CertificationNotFoundException::new);
    }

    private User getUser(String oauthId) {
        return userRepository.findByOauthId(oauthId)
                .orElseThrow(UserNotFoundException::new);
    }

    private void validateCertificationExists(Long certificationId) {
        if (!certificationRepository.existsById(certificationId)) {
            throw new CertificationNotFoundException();
        }
    }

    private Comment getCommentByAuthorizedUser(Long commentId, String oauthId) {
        User findUser = getUser(oauthId);
        Comment comment = getComment(commentId);
        if (Objects.equals(comment.getUser().getId(), findUser.getId()))
            return comment;
        else throw new UserUnauthorizedException();
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
    }

}
