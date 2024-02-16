package dnd.donworry.service.impl;

import dnd.donworry.domain.dto.comment.CommentRequestDto;
import dnd.donworry.domain.dto.comment.CommentResponseDto;
import dnd.donworry.domain.entity.Comment;
import dnd.donworry.domain.entity.User;
import dnd.donworry.domain.entity.Vote;
import dnd.donworry.repository.CommentRepository;
import dnd.donworry.repository.UserRepository;
import dnd.donworry.repository.VoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private CommentRepository commentRepository;

    @Transactional
    @Test
    @DisplayName("댓글 생성 성공 테스트")
    void createComment_Success() {

        //given
        CommentRequestDto requestDto = new CommentRequestDto("테스트 댓글 내용");

        User user = User.builder().id(1L).email("test@test.com").avatar("1").nickname("test").build();

        Vote vote = Vote.builder()
                .id(1L).content("1234").likes(0).user(user).closeDate(LocalDateTime.now()).status(false).title("test").views(0).voters(0).build();
        when(voteRepository.findById(vote.getId())).thenReturn(Optional.of(vote));

        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Comment comment = Comment.builder().id(3L).vote(vote).user(user).likes(0).content("testtest").build();
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // when
        CommentResponseDto responseDto = commentService.createComment(requestDto, vote.getId(), user.getEmail());

        // then
        assertThat(responseDto.getCommentId()).isEqualTo(comment.getId());

    }

    @Transactional
    @Test
    @DisplayName("댓글 업데이트 성공 테스트")
    void updateComment_Success() {
        //given
        User user = User.builder().id(1L).email("test@test.com").avatar("1").nickname("test").build();
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Vote vote = Vote.builder()
                .id(1L).content("1234").likes(0).user(user).closeDate(LocalDateTime.now()).status(false).title("test").views(0).voters(0).build();
        Mockito.lenient().when(voteRepository.findById(vote.getId())).thenReturn(Optional.of(vote));

        Comment savedComment = Comment.builder().id(3L).vote(vote).user(user).likes(0).content("testtest").build();
        Mockito.lenient().when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        CommentRequestDto updateRequestDto = new CommentRequestDto("수정된 댓글 내용");
        when(commentRepository.findById(savedComment.getId())).thenReturn(Optional.of(savedComment)); // 변경된 부분

        // when
        CommentResponseDto updatedResponseDto = commentService.updateComment(updateRequestDto, savedComment.getId(), user.getEmail());

        // then
        assertThat(updatedResponseDto).isNotNull();
        assertThat(savedComment.getContent()).isEqualTo(updatedResponseDto.getContent());
    }
}