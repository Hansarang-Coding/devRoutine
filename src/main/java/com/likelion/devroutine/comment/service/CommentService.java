package com.likelion.devroutine.comment.service;

import com.likelion.devroutine.alarm.domain.Alarm;
import com.likelion.devroutine.alarm.enumurate.AlarmType;
import com.likelion.devroutine.alarm.repository.AlarmRepository;
import com.likelion.devroutine.challenge.enumerate.ResponseMessage;
import com.likelion.devroutine.comment.domain.Comment;
import com.likelion.devroutine.comment.dto.*;
import com.likelion.devroutine.comment.exception.CommentNotFoundException;
import com.likelion.devroutine.user.exception.UserNotFoundException;
import com.likelion.devroutine.user.exception.UserUnauthorizedException;
import com.likelion.devroutine.comment.repository.CommentRepository;
import com.likelion.devroutine.user.domain.User;
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
    private final AlarmRepository alarmRepository;

    //private final Certification certification;

    @Transactional
    public CommentCreateResponse createComment(Long certificationId, CommentRequest request, String oauthId) {
        //Certification certification = findCertification(CertificationId)
        User user = getOauthId(oauthId);

        //댓글작성
        //commentAlarm(certificationId, name);
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

    //알람 엔티티 추가 메서드
    private void commentAlarm(Long certificationWriteId, String commentWriteName) {
        //certification은 글쓴이 / 댓글작성자는 name
        User oauthUser = getOauthId(commentWriteName);
        Long commentWriteNameId = oauthUser.getId();

        //글쓴이
        //User certificationUser = getCertification(certificationWriteId).getUser();

//        alarmRepository.save(Alarm.commentAlarm( commentWriteNameId,
//                AlarmType.NEW_COMMENT_ON_CERTIFICATION.getMessage(), AlarmType.NEW_COMMENT_ON_CERTIFICATION, certificationUser));
    }


//    private Certification findCertification(Long certificationId) {
//        return CertificationRepository.findById(CertificationId).orElseThrow(() ->
//                new CertificationNotFoundException());
//    }



    private User getOauthId(String OauthId) {
        return userRepository.findByOauthId(OauthId)
                .orElseThrow(()-> new UserNotFoundException());
    }


    //    private void validateCertificationExists(Long certificationId){
//        if(!Repository.existsById(certificationId)){
//            throw new CertificationNotFoundException()
//    }
//}

    private Comment getCommentByAuthorizedUser(Long commentId, String userName) {
        User findUser = getOauthId(userName);
        Comment comment = getComment(commentId);
        if (Objects.equals(comment.getUser().getId(), findUser.getId()))
            return comment;
        else throw new UserUnauthorizedException();
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new CommentNotFoundException());
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }


}
