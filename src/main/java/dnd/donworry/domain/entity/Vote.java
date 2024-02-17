package dnd.donworry.domain.entity;

import java.time.LocalDateTime;

import dnd.donworry.domain.constants.Category;
import dnd.donworry.domain.dto.vote.VoteRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Category category;

	public static Vote toEntity(VoteRequestDto voteRequestDto, User user) {
		return Vote.builder()
			.user(user)
			.title(voteRequestDto.getTitle())
			.content(voteRequestDto.getContent())
			.closeDate(voteRequestDto.getCloseDate())
			.build();
	}

}
