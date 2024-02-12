package dnd.donworry.domain.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @ManyToOne
    private Comment comment;


    @ManyToOne
    private User user;

    @Column(nullable = false)
    private boolean status = true;
}
