package dnd.donworry.domain.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pages {
    long getTotalPages;
    long getTotalElements;

    int getNumber;
    boolean hasPrevious;

    boolean hasNext;

    public static Pages of(long getTotalPages, long getTotalElements, boolean hasPrevious, boolean hasNext, int getNumber) {
        return Pages.builder()
                .getNumber(getNumber)
                .getTotalPages(getTotalPages)
                .getTotalElements(getTotalElements)
                .hasNext(hasNext)
                .hasPrevious(hasPrevious)
                .build();
    }
}
