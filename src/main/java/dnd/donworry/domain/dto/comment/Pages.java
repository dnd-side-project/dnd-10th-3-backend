package dnd.donworry.domain.dto.comment;

import dnd.donworry.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

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

    public static Pages of(Page<Comment> page) {
        return Pages.builder()
                .getNumber(page.getNumber())
                .getTotalPages(page.getTotalPages())
                .getTotalElements(page.getTotalElements())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
}
