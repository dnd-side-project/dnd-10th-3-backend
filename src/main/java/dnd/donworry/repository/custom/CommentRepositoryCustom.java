package dnd.donworry.repository.custom;

import dnd.donworry.domain.dto.comment.CommentResponseReadDto;
import dnd.donworry.domain.entity.Vote;

import java.util.List;

public interface CommentRepositoryCustom {
    List<CommentResponseReadDto> findCommentsByVote(Vote vote, Long lastCommentId, int size, String email);
}
