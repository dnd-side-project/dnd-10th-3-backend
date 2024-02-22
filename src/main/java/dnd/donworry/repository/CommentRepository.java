package dnd.donworry.repository;

import dnd.donworry.domain.entity.Comment;
import dnd.donworry.repository.custom.CommentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
}
