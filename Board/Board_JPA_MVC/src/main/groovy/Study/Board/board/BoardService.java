package Study.Board.board;

import Study.Board.board.dtos.BoardUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public List<Board> readAll() {
        return boardRepository.findAll();
    }

    @Transactional
    public Board create(Board board) {
        return boardRepository.save(board);
    }

    public Board readDetail(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new NoSuchElementException("No Board Data")
        );
    }

    // 변경 감지로 update
    @Transactional
    public Board update(Long boardId, BoardUpdateDto dto) {
        Board findBoard = boardRepository.findById(boardId).orElse(null);
        if (findBoard == null) {
            throw new NoSuchElementException("No Board Data");
        }

        // 영속성 컨텍스트에서 영속화된 엔티티를 확인 후 변경이 있다면 쓰기 지연 저장소에 update 쿼리를 넣는다.
        // 트랜잭션 종료 시 쓰기 지연 저장소의 update 쿼리가 DB에 전달된다.
        findBoard.update(dto.getTitle(), dto.getContent());

        return findBoard;
    }
}
