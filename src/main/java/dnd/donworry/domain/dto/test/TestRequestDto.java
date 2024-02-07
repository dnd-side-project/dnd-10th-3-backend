package dnd.donworry.domain.dto.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
public class TestRequestDto {
    private String buddy;
    private Long trust;
    private Long love;
    private Long talk;
}
