package net.nonworkspace.demo.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.nonworkspace.demo.domain.Board;
import net.nonworkspace.demo.domain.Comment;
import net.nonworkspace.demo.domain.dto.board.BoardDto;
import net.nonworkspace.demo.domain.dto.board.BoardFormDto;
import net.nonworkspace.demo.domain.dto.board.BoardViewDto;
import net.nonworkspace.demo.domain.dto.board.CommentDto;
import net.nonworkspace.demo.exception.common.CommonBizException;
import net.nonworkspace.demo.exception.common.CommonBizExceptionCode;
import net.nonworkspace.demo.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public List<BoardDto> findBoards(String title) {
        List<Board> boards = (title == null || title.isEmpty()) ? boardRepository.findAll()
            : boardRepository.findAll(title);
        List<BoardDto> result = new ArrayList<>();
        boards.forEach(b -> result.add(new BoardDto(b)));
        return result;
    }

    public BoardViewDto findBoard(Long boardId) {
        Board board = boardRepository.find(boardId);
        if (board == null) {
            throw new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND);
        }
        return new BoardViewDto(board);
    }

    @Transactional
    public Long post(BoardFormDto boardFormDto) {
        Board board = Board.createBoard(
            boardFormDto.boardId(),
            boardFormDto.title(),
            boardFormDto.content(),
            boardFormDto.writerId()
        );
        return boardRepository.save(board);
    }

    @Transactional
    public Board editBoard(Board board) {
        Board target = boardRepository.find(board.getBoardId());
        if (target == null) {
            throw new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND);
        }
        target.setContent(board.getContent());
        // target.setComments(board.getComments());
        boardRepository.save(target);

        return target;
    }

    @Transactional
    public Long deleteBoard(Long boardId) {
        Board target = boardRepository.find(boardId);
        if (target == null) {
            throw new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND);
        }
        boardRepository.delete(target.getBoardId());
        return 1L;
    }

    @Transactional
    public Long postComment(Long boardId, CommentDto commentDto) {
        Board board = boardRepository.find(boardId);
        if (board == null) {
            throw new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND);
        }
        Comment comment = Comment.createComment(
            commentDto.commentId(),
            board,
            commentDto.content(),
            commentDto.writerId()
        );
        boardRepository.saveComment(comment);
        return comment.getCommentId();
    }

    @Transactional
    public Long deleteComment(Long boardId, Long commentId) {
        Board board = boardRepository.find(boardId);
        if (board == null) {
            throw new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND);
        }

        Comment comment = boardRepository.findComment(boardId, commentId);
        if (comment == null) {
            throw new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND);
        }
        boardRepository.deleteComment(boardId, commentId);
        return 1L;
    }
}
