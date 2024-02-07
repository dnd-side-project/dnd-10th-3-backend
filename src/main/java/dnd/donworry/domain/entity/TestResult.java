package dnd.donworry.domain.entity;

import dnd.donworry.domain.dto.test.TestResponseDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TestResult {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String buddy;

    @Column(nullable = false)
    private Long trust;

    @Column(nullable = false)
    private Long love;

    @Column(nullable = false)
    private Long talk;

    @Column(nullable = false)
    private Long temperature;

    @Column(nullable = false)
    private String imageUrl;

    public static TestResult toEntity (String username, TestResponseDto testResponseDto) {
        return TestResult.builder()
                .username(username)
                .buddy(testResponseDto.getBuddy())
                .trust(testResponseDto.getTrust())
                .love(testResponseDto.getLove())
                .talk(testResponseDto.getTalk())
                .temperature(testResponseDto.getTemperature())
                .imageUrl(testResponseDto.getImageUrl())
                .build();
    }
}
