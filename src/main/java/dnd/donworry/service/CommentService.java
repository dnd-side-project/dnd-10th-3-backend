package dnd.donworry.service;

import dnd.donworry.domain.dto.comment.CommentPagingDto;
import dnd.donworry.domain.dto.comment.CommentRequestDto;
import dnd.donworry.domain.dto.comment.CommentResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {

    CommentResponseDto createComment(CommentRequestDto commentRequestDto, Long voteId, String email);

    CommentResponseDto updateComment(CommentRequestDto commentRequestDto, Long commentId, String email);

    void deleteComment(Long commentId, String email);

    CommentResponseDto updateEmpathy(Long commentId, String email);

    CommentPagingDto getComments(String email, Long voteId, Pageable pageable);

    List<CommentResponseDto> findAllComments(String email, Long voteId);
}
