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

    long totalPages;

    long totalElements;

    int currentPage;

    boolean hasPrevious;

    boolean hasNext;

    public static Pages of(Page<Comment> page) {
        return Pages.builder()
                .currentPage(page.getNumber())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
}
