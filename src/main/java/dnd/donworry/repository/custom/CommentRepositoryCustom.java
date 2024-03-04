package dnd.donworry.repository.custom;

import dnd.donworry.domain.entity.Comment;
import dnd.donworry.domain.entity.Vote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentRepositoryCustom {
    Page<Comment> findCommentsByVote(Vote vote, Pageable pageable);

    List<Comment> findAllCustom(Long voteId);
}
