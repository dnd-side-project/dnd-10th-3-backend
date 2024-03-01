package dnd.donworry.domain.dto.comment;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	public static <T> Pages of(Page<T> page) {
		return Pages.builder()
			.currentPage(page.getNumber())
			.totalPages(page.getTotalPages())
			.totalElements(page.getTotalElements())
			.hasNext(page.hasNext())
			.hasPrevious(page.hasPrevious())
			.build();
	}
}
