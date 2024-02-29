package dnd.donworry.repository.impl;

import dnd.donworry.domain.entity.Comment;
import dnd.donworry.domain.entity.Vote;
import dnd.donworry.repository.Support.Querydsl4RepositorySupport;
import dnd.donworry.repository.custom.CommentRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static dnd.donworry.domain.entity.QComment.comment;


public class CommentRepositoryImpl extends Querydsl4RepositorySupport implements CommentRepositoryCustom {
    public CommentRepositoryImpl() {
        super(Comment.class);
    }

    @Override
    public Page<Comment> findCommentsByVote(Vote voteEntity, Pageable pageable) {
        List<Comment> comments = selectFrom(comment)
                .where(comment.vote.eq(voteEntity))
                .orderBy(comment.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = select(comment.id.count())
                .from(comment)
                .where(comment.vote.eq(voteEntity))
                .fetchOne();

        long totalCount = (total != null) ? total : 0L;

        return new PageImpl<>(comments, pageable, totalCount);
    }
}
