package net.nonworkspace.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.dto.board.BoardFormDto;
import net.nonworkspace.demo.domain.dto.board.BoardViewDto;
import net.nonworkspace.demo.domain.dto.board.CommentDto;
import net.nonworkspace.demo.domain.dto.user.JoinRequestDto;
import net.nonworkspace.demo.domain.dto.user.UserInfoDto;
import net.nonworkspace.demo.domain.entity.Comment;
import net.nonworkspace.demo.exception.common.CommonBizException;
import net.nonworkspace.demo.exception.common.CommonBizExceptionCode;
import net.nonworkspace.demo.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Slf4j
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private MemberJpaService memberJpaService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;

    private UserInfoDto testUser1;

    private UserInfoDto testUser2;

    @BeforeEach
    void setUp() {
        this.testUser1 = this.getTestUser("test1", "test1@aaa.ddd");
        this.testUser2 = this.getTestUser("test2", "test2@aaa.ddd");
    }

    @Test
    @DisplayName("게시물 등록하고 조회 했을 때, Not Null 이면 성공")
    void post() {
        // given
        BoardFormDto newBoardForm = getTestBoardFormDto();

        // when
        Long boardId = boardService.post(newBoardForm, testUser1);

        // then
        assertThat((boardService.findBoard(boardId).boardId())).as(
                "등록한 게시물 ID 로 조회했을 때, Not Null이어야 한다.")
            .isEqualTo(boardId);
    }

    @Test
    @DisplayName("게시물 삭제하고 조회했을 때, \"데이터가 존재하지 않습니다.\" 예외 발생해야 성공")
    void deleteBoard() {
        // given
        BoardFormDto newBoardForm = getTestBoardFormDto();
        Long boardId = boardService.post(newBoardForm, testUser1);

        // when
        boardService.deleteBoard(boardId, testUser1);
        Exception e = assertThrows(CommonBizException.class, () -> {
            boardService.findBoard(boardId);
        });

        // then
        assertThat(e.getMessage()).as("\"데이터가 존재하지 않습니다.\" 예외가 발생해야 한다").isEqualTo(
            CommonBizExceptionCode.DATA_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("testUser1 이 작성한 게시물을 testUser2 가 삭제할려고 했을 때, \"접근 권한이 없습니다.\" 예외가 발생해야 한다")
    void testUser2CannotDeleteBoardWrittenByTestUser1() {
        // given
        BoardFormDto newBoardForm = getTestBoardFormDto();
        Long boardId = boardService.post(newBoardForm, testUser1);

        // when
        Exception e = assertThrows(CommonBizException.class, () -> {
            boardService.deleteBoard(boardId, testUser2);
        });

        // then
        assertThat(e.getMessage()).as("\"접근 권한이 없습니다.\" 예외가 발생해야 한다").isEqualTo(
            CommonBizExceptionCode.ACCESS_NOT_ALLOWED.getMessage());
    }

    @Test
    @DisplayName("게시물 삭제할 때, 게시물의 댓글도 전부 삭제되어야 성공")
    void commentsMustBeDeletedWhenBoardIsDeleted() {
        // given
        BoardFormDto newBoardForm = getTestBoardFormDto();
        Long boardId = boardService.post(newBoardForm, testUser1);
        boardService.postComment(boardId, getTestCommentDto("test comment1"), testUser1);
        boardService.postComment(boardId, getTestCommentDto("test comment2"), testUser2);
        BoardViewDto board = boardService.findBoard(boardId);
        log.debug("게시물 삭제 전 댓글 갯수: {}", board.comments().size());

        // when
        boardService.deleteBoard(boardId, testUser1);

        // then
        String query = "select c from Comment c where c.board.boardId = :boardId";
        List<Comment> result = em.createQuery(query, Comment.class).setParameter("boardId", boardId)
            .getResultList();
        log.debug("게시물 삭제 후 댓글 갯수: {}", result.size());

        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("댓글 등록하고 게시물 조회 했을 때, 조회 결과에 댓글 ID 를 가진 댓글 데이터가 존재해야 성공")
    void postComment() {
        // given
        BoardFormDto newBoardForm = getTestBoardFormDto();
        Long boardId = boardService.post(newBoardForm, testUser1);
        Long newCommentId = boardService.postComment(boardId, getTestCommentDto("test comment1"),
            testUser1);
        log.debug("new comment id: {}", newCommentId);

        // when
        BoardViewDto board = boardService.findBoard(boardId);

        // then
        assertThat(
            board.comments().stream().filter(c -> c.commentId().equals(newCommentId)).findAny()
                .isPresent()).isTrue();
    }

    @Test
    @DisplayName("댓글 삭제하고 게시물 조회 했을 때, 조회 결과에 댓글 ID 를 가진 댓글 데이터가 존재하지 않아야 성공")
    void deleteComment() {
        // given
        BoardFormDto newBoardForm = getTestBoardFormDto();
        Long boardId = boardService.post(newBoardForm, testUser1);
        Long commentId = boardService.postComment(boardId, getTestCommentDto("test comment1"),
            testUser1);
        boardService.postComment(boardId, getTestCommentDto("test comment2"),
            testUser2);
        BoardViewDto board = boardService.findBoard(boardId);
        int commentCountBefore = board.comments().size();
        log.debug("댓글 삭제 전 댓글 갯수: {}", commentCountBefore); // 2건

        // when
        boardService.deleteComment(boardId, commentId, testUser1);

        // then
        String query = "select c from Comment c where c.board.boardId = :boardId";
        List<Comment> result = em.createQuery(query, Comment.class).setParameter("boardId", boardId)
            .getResultList();
        log.debug("댓글 삭제 후 댓글 갯수: {}", result.size());
        assertThat(
            result.stream().filter(c -> c.equals(commentId)).findAny().isPresent()).isFalse();
        assertThat(result.size()).isEqualTo(commentCountBefore - 1);    // 1건
    }

    @Test
    @DisplayName("testUser1 이 작성한 댓글을 testUser2 가 삭제할려고 했을 때, \"접근 권한이 없습니다.\" 예외가 발생해야 한다")
    void testUser2CannotDeleteCommentWrittenByTestUser1() {
        // given
        BoardFormDto newBoardForm = getTestBoardFormDto();
        Long boardId = boardService.post(newBoardForm, testUser1);
        Long commentId = boardService.postComment(boardId, getTestCommentDto("test comment1"),
            testUser1);

        // when
        Exception e = assertThrows(CommonBizException.class,
            () -> boardService.deleteComment(boardId, commentId, testUser2));

        // then
        assertThat(e.getMessage()).isEqualTo(
            CommonBizExceptionCode.ACCESS_NOT_ALLOWED.getMessage());
    }

    private UserInfoDto getTestUser(String name, String email) {
        return new UserInfoDto(memberRepository.find(
            memberJpaService.join(new JoinRequestDto(name, email, "Rkskekfk1!", "Rkskekfk1!"))));
    }

    private BoardFormDto getTestBoardFormDto() {
        return new BoardFormDto(
            "test title",
            "test content"
        );
    }

    private CommentDto getTestCommentDto(String content) {
        return new CommentDto(
            null,
            content,
            null,
            LocalDateTime.now()
        );
    }
}