package dnd.donworry.repository.custom;

import dnd.donworry.domain.entity.Comment;
import dnd.donworry.domain.entity.Vote;

import java.util.List;

public interface CommentRepositoryCustom {
    List<Comment> findCommentsByVote(Vote vote, Long lastCommentId, int size);
}
