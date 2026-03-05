package Study.Board.comment;

import Study.Board.board.Board;
import Study.Board.board.BoardRepository;
import Study.Board.comment.dtos.CommentReadResDto;
import Study.Board.comment.dtos.CommentSaveReqDto;
import Study.Board.comment.dtos.CommentUpdateReqDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public List<CommentReadResDto> findByBoardId(Long boardId) {
        List<Comment> comments = commentRepository.findByBoardId(boardId);
        return entitiesToDtosForView(comments);
    }

    @Transactional
    public void save(CommentSaveReqDto dto, Long boardId) {
        Comment comment = dtoToEntityForSave(dto);
        comment.encodePassword(passwordEncoder.encode(comment.getPassword())); // 비밀번호 암호화

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new EntityNotFoundException("cannot find board"));

        board.addComment(comment); // 연관관계 편의 메서드

        commentRepository.save(comment);
    }

    @Transactional
    public void update(Long commentId, CommentUpdateReqDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException("cannot find comment"));
        comment.update(dto);
    }

    @Transactional
    public void delete(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException("cannot find comment"));
        commentRepository.delete(comment);
    }

    private List<CommentReadResDto> entitiesToDtosForView(List<Comment> comments) {
        List<CommentReadResDto> dtos = new ArrayList<>();
        for (Comment c : comments) {
            dtos.add(CommentReadResDto.builder()
                    .commentId(c.getId())
                    .comment(c.getComment())
                    .build());
        }
        return dtos;
    }

    private Comment dtoToEntityForSave(CommentSaveReqDto dto) {
        return Comment.builder()
                .comment(dto.getComment())
                .password(dto.getPassword())
                .build();
    }

    public boolean verifyPassword(Long commentId, String rawPassword) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException("cannot find comment"));
        // 입력받은 평문 비밀번호와 DB의 암호화된 비밀번호 비교
        return passwordEncoder.matches(rawPassword, comment.getPassword());
    }
}
