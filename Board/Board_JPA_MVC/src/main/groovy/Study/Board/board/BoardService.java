package Study.Board.board;

import Study.Board.board.dtos.BoardReadResDto;
import Study.Board.board.dtos.BoardSaveReqDto;
import Study.Board.board.dtos.BoardUpdateReqDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public BoardReadResDto save(BoardSaveReqDto dto) {
        Board board = dtoToEntity(dto);
        board.encodePassword(passwordEncoder.encode(board.getPassword()));
        return entityToDtoForView(boardRepository.save(board));
    }

    public BoardReadResDto findByIdDto(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new EntityNotFoundException("cannot find board")
        );
        return entityToDtoForView(board);
    }

    public Page<BoardReadResDto> findAllByPageDto(int page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "id"));
        Page<Board> boards = boardRepository.findAll(pageable);
        Page<BoardReadResDto> boardReadDtos = entitiesToDtosForView(boards);
        return boardReadDtos;
    }

    // 변경 감지로 update
    @Transactional
    public void update(Long boardId, BoardUpdateReqDto dto) {
        Board findBoard = boardRepository.findById(boardId).orElseThrow(() ->
                new EntityNotFoundException("cannot find board"));
        // 영속성 컨텍스트에서 영속화된 엔티티를 확인 후 변경이 있다면 쓰기 지연 저장소에 update 쿼리를 넣는다.
        // 트랜잭션 종료 시 쓰기 지연 저장소의 update 쿼리가 DB에 전달된다.
        findBoard.update(dto);
    }

    @Transactional
    public void delete(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("cannot find board"));
        boardRepository.delete(board);
    }

    public boolean verifyPassword(Long boardId, String rawPassword) {
        return passwordEncoder.matches(rawPassword, boardRepository.findById(boardId).orElseThrow(() ->
            new EntityNotFoundException("cannot find board"))
                .getPassword());
    }

    private BoardReadResDto entityToDtoForView(Board board) {
        return BoardReadResDto.builder()
                .boardId(board.getId())
                .content(board.getContent())
                .title(board.getTitle())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

    private Page<BoardReadResDto> entitiesToDtosForView(Page<Board> boards) {
        return boards.map(b -> BoardReadResDto.builder()
                .boardId(b.getId())
                .title(b.getTitle())
                .content(b.getContent())
                .createdAt(b.getCreatedAt())
                .updatedAt(b.getUpdatedAt())
                .build());
    }

    private Board dtoToEntity(BoardSaveReqDto dto) {
        return Board.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .password(dto.getPassword())
                .build();
    }
}
