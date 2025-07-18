package net.nonworkspace.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.dto.board.BoardDto;
import net.nonworkspace.demo.domain.dto.board.BoardFormDto;
import net.nonworkspace.demo.domain.dto.board.BoardViewDto;
import net.nonworkspace.demo.domain.dto.board.CommentDto;
import net.nonworkspace.demo.domain.dto.common.ListResponse;
import net.nonworkspace.demo.domain.dto.user.UserInfoDto;
import net.nonworkspace.demo.domain.entity.Board;
import net.nonworkspace.demo.domain.entity.Comment;
import net.nonworkspace.demo.exception.common.CommonBizException;
import net.nonworkspace.demo.exception.common.CommonBizExceptionCode;
import net.nonworkspace.demo.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;

    public ListResponse<BoardDto> findBoards(String title) {
        List<Board> boards = (title == null || title.isEmpty()) ? boardRepository.findAll()
                : boardRepository.findAll(title);
        List<BoardDto> result = new ArrayList<>();
        boards.forEach(b -> result.add(new BoardDto(b)));
        return new ListResponse<>(result);
    }

    public BoardViewDto findBoard(Long boardId) {
        Board board = Optional.ofNullable(boardRepository.find(boardId))
                .orElseThrow(() -> new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND));
        return new BoardViewDto(board);
    }

    @Transactional
    public Long post(BoardFormDto boardFormDto, UserInfoDto loginUserInfo) {
        Board board = Board.createBoard(
                boardFormDto.title(),
                boardFormDto.content(),
                loginUserInfo.userId()
        );
        return boardRepository.save(board);
    }

    @Transactional
    public Board editBoard(Board board) {
        Board target = Optional.ofNullable(boardRepository.find(board.getBoardId()))
                .orElseThrow(() -> new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND));
        target.updateContent(board.getContent());
        // boardRepository.save(target);    // dirty checking

        return target;
    }

    @Transactional
    public Long deleteBoard(Long boardId, UserInfoDto loginUserInfo) throws CommonBizException {
        Board target = Optional.ofNullable(boardRepository.find(boardId))
                .orElseThrow(() -> new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND));
        if (!isEditableUser(target.getWriter().getWriterId(), loginUserInfo)) {
            throw new CommonBizException(CommonBizExceptionCode.ACCESS_NOT_ALLOWED);
        };
        boardRepository.delete(target.getBoardId());
        return 1L;
    }

    @Transactional
    public Long postComment(Long boardId, CommentDto commentDto, UserInfoDto loginUserInfo) {
        Board board = Optional.ofNullable(boardRepository.find(boardId))
                .orElseThrow(() -> new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND));
        Comment comment = Comment.createComment(
                board,
                commentDto.content(),
                loginUserInfo.userId()
        );
        boardRepository.saveComment(comment);
        return comment.getCommentId();
    }

    @Transactional
    public Long deleteComment(Long boardId, Long commentId, UserInfoDto loginUserInfo) throws CommonBizException {
        Comment comment = Optional.ofNullable(boardRepository.findComment(boardId, commentId))
                .orElseThrow(() ->
                        new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND)
                );
        if (!isEditableUser(comment.getWriter().getWriterId(), loginUserInfo)) {
            throw new CommonBizException(CommonBizExceptionCode.ACCESS_NOT_ALLOWED);
        };
        boardRepository.deleteComment(boardId, commentId);
        return 1L;
    }

    private boolean isEditableUser(Long writerId, UserInfoDto loginUserInfo) {
        return writerId.equals(loginUserInfo.userId()) || loginUserInfo.hasAdminRole();

    }
}
