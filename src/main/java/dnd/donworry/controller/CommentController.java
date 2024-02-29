package dnd.donworry.controller;

import dnd.donworry.domain.constants.ResResult;
import dnd.donworry.domain.constants.ResponseCode;
import dnd.donworry.domain.dto.comment.CommentPagingDto;
import dnd.donworry.domain.dto.comment.CommentRequestDto;
import dnd.donworry.domain.dto.comment.CommentResponseDto;
import dnd.donworry.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "댓글 API", description = "댓글을 생성, 조회, 수정, 삭제합니다.")
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/vote/{voteId}")
    @Operation(summary = "댓글 생성", description = "인증된 회원이 댓글을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 생성 성공"),
            @ApiResponse(responseCode = "404", description = "댓글 생성 실패", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"code\": \"404\", \n \"message\": \"입력값이 잘못되었습니다.\"\n}"))),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"code\": \"403\", \n \"message\": \"접근 권한이 없습니다.\"\n}"))),
            @ApiResponse(responseCode = "401", description = "토큰이 존재하지 않음", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"code\": \"401\", \n \"message\": \"유효한 토큰이 존재하지 않습니다.\"\n}"))),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"code\": \"500\", \n \"message\": \"서버에 에러가 발생했습니다.\"\n}")))
    })
    ResResult<CommentResponseDto> create(@RequestBody CommentRequestDto commentRequestDto,
                                         @PathVariable(name = "voteId") Long voteId,
                                         Authentication authentication, HttpServletResponse response) {
        return ResponseCode.COMMENT_CREATE.toResponse(
                commentService.createComment(commentRequestDto, voteId, authentication.getName()), response);
    }

    @PatchMapping("/{commentId}")
    @Operation(summary = "댓글 수정", description = "인증된 회원이 댓글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공"),

            @ApiResponse(responseCode = "404", description = "댓글 수정 실패", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"code\": \"404\", \n \"message\": \"입력값이 잘못되었습니다.\"\n}"))),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"code\": \"403\", \n \"message\": \"접근 권한이 없습니다.\"\n}"))),
            @ApiResponse(responseCode = "401", description = "토큰이 존재하지 않음", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"code\": \"401\", \n \"message\": \"유효한 토큰이 존재하지 않습니다.\"\n}"))),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"code\": \"500\", \n \"message\": \"서버에 에러가 발생했습니다.\"\n}")))
    })
    ResResult<CommentResponseDto> update(@RequestBody CommentRequestDto commentRequestDto,
                                         @PathVariable(name = "commentId") Long commentId,
                                         Authentication authentication, HttpServletResponse response) {
        return ResponseCode.COMMENT_UPDATE.toResponse(
                commentService.updateComment(commentRequestDto, commentId, authentication.getName()), response);
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제", description = "인증된 회원이 댓글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "댓글 삭제 실패", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"code\": \"404\", \n \"message\": \"입력값이 잘못되었습니다.\"\n}"))),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"code\": \"403\", \n \"message\": \"접근 권한이 없습니다.\"\n}"))),
            @ApiResponse(responseCode = "401", description = "토큰이 존재하지 않음", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"code\": \"401\", \n \"message\": \"유효한 토큰이 존재하지 않습니다.\"\n}"))),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"code\": \"500\", \n \"message\": \"서버에 에러가 발생했습니다.\"\n}")))
    })
    ResResult<CommentResponseDto> delete(@PathVariable(name = "commentId") Long commentId,
                                         Authentication authentication, HttpServletResponse response) {
        commentService.deleteComment(commentId, authentication.getName());
        return ResponseCode.COMMENT_DELETE.toResponse(null, response);
    }

    @PostMapping("/{commentId}/likes")
    @Operation(summary = "공감 생성, 취소 ", description = "인증된 회원이 공감을 생성, 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공감 성공"),
            @ApiResponse(responseCode = "404", description = "공감 실패", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"code\": \"404\", \n \"message\": \"입력값이 잘못되었습니다.\"\n}"))),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"code\": \"403\", \n \"message\": \"접근 권한이 없습니다.\"\n}"))),
            @ApiResponse(responseCode = "401", description = "토큰이 존재하지 않음", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"code\": \"401\", \n \"message\": \"유효한 토큰이 존재하지 않습니다.\"\n}"))),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"code\": \"500\", \n \"message\": \"서버에 에러가 발생했습니다.\"\n}")))
    })
    ResResult<CommentResponseDto> empathy(@PathVariable(name = "commentId") Long commentId,
                                          Authentication authentication, HttpServletResponse response) {
        CommentResponseDto empathy = commentService.updateEmpathy(commentId, authentication.getName());
        return empathy.isStatus() ? ResponseCode.LIKES_ADD.toResponse(empathy, response)
                : ResponseCode.LIKES_CANCEL.toResponse(empathy, response);

    }

    @GetMapping("/{voteId}")
    @Operation(summary = "댓글 페이징 조회 ", description = "인증된 회원이 댓글을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "조회 실패", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"code\": \"404\", \n \"message\": \"입력값이 잘못되었습니다.\"\n}"))),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"code\": \"403\", \n \"message\": \"접근 권한이 없습니다.\"\n}"))),
            @ApiResponse(responseCode = "401", description = "토큰이 존재하지 않음", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"code\": \"401\", \n \"message\": \"유효한 토큰이 존재하지 않습니다.\"\n}"))),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"code\": \"500\", \n \"message\": \"서버에 에러가 발생했습니다.\"\n}")))
    })
    ResResult<CommentPagingDto> getComments(@PathVariable(name = "voteId") Long voteId,
                                            @PageableDefault(sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable,
                                            Authentication authentication, HttpServletResponse response) {
        return ResponseCode.COMMENT_READ.toResponse(
                commentService.getComments(authentication.getName(), voteId, pageable), response);
    }

}
