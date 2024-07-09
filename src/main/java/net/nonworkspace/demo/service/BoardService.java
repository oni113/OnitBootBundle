package net.nonworkspace.demo.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import net.nonworkspace.demo.domain.Board;
import net.nonworkspace.demo.domain.Comment;
import net.nonworkspace.demo.exception.common.CommonBizException;
import net.nonworkspace.demo.exception.common.CommonBizExceptionCode;
import net.nonworkspace.demo.repository.BoardRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public List<Board> findBoards(String title) {
        return (title == null || title.isEmpty()) ? boardRepository.findAll()
                : boardRepository.findAll(title);
    }
    public Board findBoard(Long boardId) {
        Board board = boardRepository.find(boardId);
        if (board == null) {
            throw new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND);
        }
        return board;
    }

    @Transactional
    public Long post(Board board) {
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
    public int deleteBoard(Long boardId) {
        Board target = boardRepository.find(boardId);
        if (target == null) {
            throw new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND);
        }
        boardRepository.delete(target.getBoardId());
        return 1;
    }

    @Transactional
    public Long postComment(Long boardId, Comment comment) {
        Board board = boardRepository.find(boardId);
        if (board == null) {
            throw new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND);
        }
        comment.setBoard(board);
        boardRepository.saveComment(comment);
        return comment.getCommentId();
    }

    @Transactional
    public int deleteComment(Long boardId, Long commentId) {
        Board board = boardRepository.find(boardId);
        if (board == null) {
            throw new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND);
        }

        Comment comment = boardRepository.findComment(boardId, commentId);
        if (comment == null) {
            throw new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND);
        }
        boardRepository.deleteComment(boardId, commentId);
        return 1;
    }
}
