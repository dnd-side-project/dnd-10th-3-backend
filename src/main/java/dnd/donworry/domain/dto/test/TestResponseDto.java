package dnd.donworry.domain.dto.test;

import lombok.*;

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

}
