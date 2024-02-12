package dnd.donworry.domain;

import dnd.donworry.domain.entity.User;
import dnd.donworry.domain.entity.Vote;
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
    private int id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Vote vote;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int likes = 0;
}
