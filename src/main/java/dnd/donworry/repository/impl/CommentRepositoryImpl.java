package dnd.donworry.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import dnd.donworry.domain.entity.Comment;
import dnd.donworry.domain.entity.Vote;
import dnd.donworry.repository.Support.Querydsl4RepositorySupport;
import dnd.donworry.repository.custom.CommentRepositoryCustom;

import java.util.List;

import static dnd.donworry.domain.entity.QComment.comment;

public class CommentRepositoryImpl extends Querydsl4RepositorySupport implements CommentRepositoryCustom {
    public CommentRepositoryImpl() {
        super(Comment.class);
    }

    @Override
    public List<Comment> findCommentsByVote(Vote vote, Long lastCommentId, int size) {
        return selectFrom(comment)
                .where(GtCommentId(lastCommentId), comment.vote.eq(vote))
                .orderBy(comment.createdAt.asc())
                .limit(size)
                .fetch();
    }
//    @Override
//    public List<CommentResponseReadDto> findCommentsByVote(Vote vote, Long lastCommentId, int size, String email) {
//        return select(Projections.fields(CommentResponseReadDto.class,
//                comment.id.as("commentId"),
//                comment.vote.id.as("voteId"),
//                comment.user.id.as("userId"),
//                comment.user.nickname,
//                comment.content,
//                comment.user.avatar,
//                comment.likes,
//                comment.createdAt,
//                comment.modifiedAt,
//                commentLike.status.as("status")))
//                .from(comment)
//                .leftJoin(commentLike)
//                .on(commentLike.comment.eq(comment).and(commentLike.user.email.eq(email)))
//                .where(GtCommentId(lastCommentId), comment.vote.eq(vote))
//                .orderBy(comment.createdAt.asc())
//                .limit(size)
//                .fetch();
//    }

    private BooleanExpression GtCommentId(Long commentId) {
        if (commentId == null) return null;
        return comment.id.gt(commentId);
    }

}
