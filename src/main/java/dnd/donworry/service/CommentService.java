package dnd.donworry.service;

import dnd.donworry.domain.dto.comment.CommentRequestDto;
import dnd.donworry.domain.dto.comment.CommentResponseDto;
import dnd.donworry.domain.dto.commentLike.CommentLikeResponseDto;

public interface CommentService {

    CommentResponseDto createComment(CommentRequestDto commentRequestDto, Long voteId, String email);

    CommentResponseDto updateComment(CommentRequestDto commentRequestDto, Long commentId, String email);

    void deleteComment(Long commentId, String email);

    CommentLikeResponseDto updateEmpathy(Long commentId, String email);
}
