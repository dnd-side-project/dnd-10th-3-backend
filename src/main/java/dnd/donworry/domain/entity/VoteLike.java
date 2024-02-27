package dnd.donworry.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class VoteLike extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Vote vote;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@Column(nullable = false)
	private boolean status;

	public static VoteLike toEntity(Vote vote, User user) {
		return VoteLike.builder()
			.vote(vote)
			.user(user)
			.status(true)
			.build();
	}

	public void updateStatus() {
		this.status = !this.status;
	}
}
