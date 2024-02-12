package dnd.donworry.domain.entity;


import dnd.donworry.domain.dto.vote.VoteRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private User user;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private int likes = 0;

	@Column(nullable = false)
	private int views = 0;

	@Column(nullable = false)
	private int voters = 0;

	@Column(nullable = false)
	private boolean status = false;

	@Column(nullable = false, updatable = false)
	private LocalDateTime closeDate;

	public static Vote toEntity(VoteRequestDto voteRequestDto, User user) {
		return Vote.builder()
			.user(user)
			.title(voteRequestDto.getTitle())
			.content(voteRequestDto.getContent())
			.closeDate(voteRequestDto.getCloseDate())
			.build();
	}

}
