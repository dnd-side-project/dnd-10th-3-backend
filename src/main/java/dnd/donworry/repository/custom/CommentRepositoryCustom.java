package dnd.donworry.repository.custom;

import dnd.donworry.domain.entity.Comment;
import dnd.donworry.domain.entity.Vote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {
    Page<Comment> findCommentsByVote(Vote vote, Pageable pageable);
}
