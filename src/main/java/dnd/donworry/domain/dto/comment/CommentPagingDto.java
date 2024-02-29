package dnd.donworry.domain.dto.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentPagingDto {
    private List<CommentResponseDto> list = new ArrayList<>();
    private Pages pages;

    public static CommentPagingDto of(List<CommentResponseDto> list, Pages pages) {
        return CommentPagingDto.builder()
                .list(list)
                .pages(pages)
                .build();
    }
}
