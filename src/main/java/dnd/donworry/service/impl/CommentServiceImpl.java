package dnd.donworry.service.impl;

import dnd.donworry.domain.constants.ErrorCode;
import dnd.donworry.domain.dto.comment.CommentRequestDto;
import dnd.donworry.domain.dto.comment.CommentResponseDto;
import dnd.donworry.domain.entity.Comment;
import dnd.donworry.domain.entity.User;
import dnd.donworry.domain.entity.Vote;
import dnd.donworry.exception.CustomException;
import dnd.donworry.repository.CommentRepository;
import dnd.donworry.repository.UserRepository;
import dnd.donworry.repository.VoteRepository;
import dnd.donworry.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final VoteRepository voteRepository;

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

    private Comment validateUserAndComment(Long commentId, String email) {
        User user = findUser(email);
        Comment comment = findComment(commentId);
        if (!user.getId().equals(comment.getUser().getId())) {
            throw new CustomException(ErrorCode.COMMENT_NOT_MATCH);
        }
        return comment;
    }

    private Comment findComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUNT));
        return comment;
    }

    private User findUser(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        return user;
    }

    private Vote findVote(Long voteId) {
        return voteRepository.findById(voteId)
                .orElseThrow(() -> new CustomException(ErrorCode.VOTE_NOT_FOUND));
    }
}
