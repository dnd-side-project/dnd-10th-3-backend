package dnd.donworry.service.impl;

import dnd.donworry.domain.constants.ErrorCode;
import dnd.donworry.domain.dto.comment.CommentRequestDto;
import dnd.donworry.domain.dto.comment.CommentResponseDto;
import dnd.donworry.domain.dto.commentLike.CommentLikeResponseDto;
import dnd.donworry.domain.entity.Comment;
import dnd.donworry.domain.entity.CommentLike;
import dnd.donworry.domain.entity.User;
import dnd.donworry.domain.entity.Vote;
import dnd.donworry.exception.CustomException;
import dnd.donworry.repository.CommentLikeRepository;
import dnd.donworry.repository.CommentRepository;
import dnd.donworry.repository.UserRepository;
import dnd.donworry.repository.VoteRepository;
import dnd.donworry.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final VoteRepository voteRepository;

    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    @Override
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, Long voteId, String email) {

        Comment savedComment = commentRepository.save(
                Comment.toEntity(findVote(voteId), findUser(email), commentRequestDto));

        return CommentResponseDto.of(savedComment);
    }



    @Transactional
    @Override
    public CommentResponseDto updateComment(CommentRequestDto commentRequestDto, Long commentId, String email) {
        Comment comment = validateUserAndComment(commentId, email);
        comment.updateContent(commentRequestDto.getContent());
        commentRepository.save(comment);

        return CommentResponseDto.of(comment);
    }

    @Transactional
    @Override
    public void deleteComment(Long commentId, String email) {
        Comment comment = validateUserAndComment(commentId, email);
        commentRepository.delete(comment);
    }

    public CommentLikeResponseDto updateEmpathy(Long commentId, String email) {
        Comment comment = findComment(commentId);
        User user = findUser(email);
        Optional<CommentLike> findCommentLike = commentLikeRepository.findByCommentIdAndUserEmail(comment.getId(), user.getEmail());

        CommentLike commentLike = findCommentLike.orElseGet(() -> CommentLike.toEntity(user, comment, false));

        savedCommentLike(commentLike);

        savedComment(comment, commentLike);

        return CommentLikeResponseDto.of(comment, commentLike);
    }

    private void savedCommentLike(CommentLike commentLike) {
        commentLike.updateStatus();
        commentLikeRepository.save(commentLike);
    }
    private void savedComment(Comment comment, CommentLike commentLike) {
        comment.updateLike(commentLike.isStatus());
        commentRepository.save(comment);
    }




    private Comment validateUserAndComment(Long commentId, String email) {
        User user = findUser(email);
        Comment comment = findComment(commentId);
        if (!user.getId().equals(comment.getUser().getId())) {
            throw new CustomException(ErrorCode.COMMENT_NOT_MATCH);
        }
        return comment;
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUNT));
    }

    private User findUser(String email) {
        return  userRepository.findUserByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Vote findVote(Long voteId) {
        return voteRepository.findById(voteId)
                .orElseThrow(() -> new CustomException(ErrorCode.VOTE_NOT_FOUND));
    }
}
