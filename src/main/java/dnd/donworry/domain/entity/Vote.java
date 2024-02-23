package dnd.donworry.domain.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dnd.donworry.domain.constants.Category;
import dnd.donworry.domain.dto.vote.VoteRequestDto;
import dnd.donworry.domain.dto.vote.VoteUpdateDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@Setter
	private User user;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vote", orphanRemoval = true)
	private List<Selection> selections = new ArrayList<>();

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
	private LocalDate closeDate;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Category category;

	public static Vote toEntity(VoteRequestDto voteRequestDto, User user) {
		return Vote.builder()
			.user(user)
			.title(voteRequestDto.getTitle())
			.content(voteRequestDto.getContent())
			.closeDate(voteRequestDto.getCloseDate())
			.category(Category.of(voteRequestDto.getCategory()))
			.selections(new ArrayList<>())
			.build();
	}

	public void update(VoteUpdateDto voteUpdateDto) {
		this.title = voteUpdateDto.getTitle();
		this.content = voteUpdateDto.getContent();
		this.category = Category.of(voteUpdateDto.getCategory());
	}

	public void addView() {
		this.views++;
	}

	public void addSelection(Selection selection) {
		this.selections.add(selection);
	}

	public void addVoter() {
		this.voters++;
	}

	public void minusVoter() {
		this.voters--;
	}
}
