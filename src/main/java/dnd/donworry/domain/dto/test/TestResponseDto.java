package dnd.donworry.domain.dto.test;

import dnd.donworry.domain.entity.TestResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TestResponseDto {
    private String buddy;
    private Long trust;
    private Long love;
    private Long talk;
    private Long temperature;
    private String imageUrl;
    private LocalDateTime createdAt;

    public static TestResponseDto of(TestResult testResult) {
        return new TestResponseDto(
                testResult.getBuddy(),
                testResult.getTrust(),
                testResult.getLove(),
                testResult.getTalk(),
                testResult.getTemperature(),
                testResult.getImageUrl(),
                testResult.getCreatedAt()
        );
    }
}
