package dnd.donworry.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
public class Selection {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "vote_id")
	private Vote vote;

	@OneToOne(mappedBy = "selection", cascade = CascadeType.ALL)
	private OptionImage optionImage;

	@Column
	private String content;

	@Column(nullable = false)
	private int count = 0;

	public static Selection toEntity(String content, Vote vote) {
		return Selection.builder()
			.content(content)
			.vote(vote)
			.build();
	}

	public Selection setOptionImage(OptionImage optionImage) {
		this.optionImage = optionImage;
		return this;
	}

	public void addCount() {
		this.count++;
	}

	public void minusCount() {
		this.count--;
	}
}
