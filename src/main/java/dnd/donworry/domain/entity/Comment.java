
package dnd.donworry.domain.entity;

import dnd.donworry.domain.dto.comment.CommentRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Vote vote;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int likes = 0;

    public static Comment toEntity(Vote vote, User user, CommentRequestDto commentRequestDto) {
        return Comment.builder()
                .content(commentRequestDto.getContent())
                .user(user)
                .vote(vote)
                .build();
    }

    public void updateContent(String content) {
        if (content != null) {
            this.content = content;
        }
    }
}
