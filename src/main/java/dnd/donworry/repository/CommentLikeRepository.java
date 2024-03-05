package dnd.donworry.repository;

import dnd.donworry.domain.entity.Comment;
import dnd.donworry.domain.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentId(Long commentId);

    void deleteByComment(Comment comment);
}
