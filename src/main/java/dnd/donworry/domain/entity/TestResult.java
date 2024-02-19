package dnd.donworry.domain.entity;

import dnd.donworry.domain.constants.PreQuestion_AGE;
import dnd.donworry.domain.constants.PreQuestion_Gender;
import dnd.donworry.domain.constants.RANK;
import dnd.donworry.domain.dto.test.TestResponseDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TestResult extends BaseEntity {
	@Id
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private User user;

	@Column(nullable = false)
	private String buddy;

	@Column(nullable = false)
	private Long trust;

	@Column(nullable = false)
	private Long love;

	@Column(nullable = false)
	private Long talk;

	@Column(nullable = false)
	private int temperature;

	@Column(nullable = false)
	private String imageUrl;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PreQuestion_AGE age;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PreQuestion_Gender gender;

	public static TestResult toEntity(User user, TestResponseDto testResponseDto) {
		return TestResult.builder()
			.age(PreQuestion_AGE.of(testResponseDto.getAge()))
			.gender(PreQuestion_Gender.of(testResponseDto.getGender()))
			.user(user)
			.buddy(testResponseDto.getBuddy())
			.trust(testResponseDto.getTrust())
			.love(testResponseDto.getLove())
			.talk(testResponseDto.getTalk())
			.temperature(RANK.of(testResponseDto.getTemperature()).getLevel())
			.imageUrl(testResponseDto.getImageUrl())
			.build();
	}

}
