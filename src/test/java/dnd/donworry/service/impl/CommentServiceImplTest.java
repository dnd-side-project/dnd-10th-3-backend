package dnd.donworry.service.impl;

import dnd.donworry.domain.dto.comment.CommentRequestDto;
import dnd.donworry.domain.dto.comment.CommentResponseDto;
import dnd.donworry.domain.dto.commentLike.CommentLikeResponseDto;
import dnd.donworry.domain.entity.Comment;
import dnd.donworry.domain.entity.CommentLike;
import dnd.donworry.domain.entity.User;
import dnd.donworry.domain.entity.Vote;
import dnd.donworry.repository.CommentLikeRepository;
import dnd.donworry.repository.CommentRepository;
import dnd.donworry.repository.UserRepository;
import dnd.donworry.repository.VoteRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    @Mock
    private CommentLikeRepository commentLikeRepository;

    private User user;
    private Comment comment;
    private Vote vote;
    @BeforeEach
    void setUp() {
         user = User.builder().id(1L).email("test@test.com").avatar("1").nickname("test").build();
         vote = Vote.builder()
                .id(1L).content("1234").likes(0).user(user).closeDate(LocalDateTime.now()).status(false).title("test").views(0).voters(0).build();
         comment = Comment.builder().id(1L).vote(vote).likes(2).content("test").build();
    }

    @Transactional
    @Test
    @DisplayName("댓글 생성 성공 테스트")
    void createComment_Success() {

        //given
        CommentRequestDto requestDto = new CommentRequestDto("테스트 댓글 내용");

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
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Mockito.lenient().when(voteRepository.findById(vote.getId())).thenReturn(Optional.of(vote));

        Comment savedComment = Comment.builder().id(3L).vote(vote).user(user).likes(0).content("testtest").build();
        Mockito.lenient().when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentRequestDto updateRequestDto = new CommentRequestDto("수정된 댓글 내용");
        when(commentRepository.findById(savedComment.getId())).thenReturn(Optional.of(savedComment)); // 변경된 부분

        // when
        CommentResponseDto updatedResponseDto = commentService.updateComment(updateRequestDto, savedComment.getId(), user.getEmail());

        // then
        assertThat(updatedResponseDto).isNotNull();
        assertThat(savedComment.getContent()).isEqualTo(updatedResponseDto.getContent());
    }

    @Test
    @Transactional
    @DisplayName("공감을 누른 경우 성공 테스트")
    void testUpdateEmpathyWhenCommentLikeExists() {
        // given
        String email = user.getEmail();
        Long id = comment.getId();
        CommentLike existingCommentLike =  CommentLike.toEntity(user,comment,true);
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.ofNullable(comment));
        when(commentLikeRepository.findByCommentIdAndUserEmail(any(), any())).thenReturn(Optional.of(existingCommentLike));

        // when
        CommentLikeResponseDto result = commentService.updateEmpathy(id, email);

        // then
        Assertions.assertThat(result.isStatus()).isEqualTo(false);
        Assertions.assertThat(result.getLikes()).isEqualTo(1);
    }

    @Test
    @DisplayName("공감을 누르지 않은 경우 성공 테스트")
    @Transactional
    void testUpdateEmpathyWhenCommentLikeDoesNotExist() {
        // given
        String email = user.getEmail();
        Long id = comment.getId();
        CommentLike existingCommentLike = CommentLike.toEntity(user, comment, false);
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.ofNullable(comment));
        when(commentLikeRepository.findByCommentIdAndUserEmail(any(), any())).thenReturn(Optional.of(existingCommentLike));

        // when
        CommentLikeResponseDto result = commentService.updateEmpathy(id, email);

        // then
        Assertions.assertThat(result.isStatus()).isEqualTo(true);
        Assertions.assertThat(result.getLikes()).isEqualTo(3);
    }
}

