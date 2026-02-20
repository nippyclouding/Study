package Study.Board.board;

import Study.Board.board.dtos.BoardUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @ExceptionHandler(Exception.class)
    public String handleExceptionElement(Exception e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error"; // 전용 에러 페이지로 이동
    }

    // =============================== READ ===============================
    @GetMapping // 페이징 x
    public String readAll(Model model) {
        List<Board> boards = boardService.readAll();

        model.addAttribute("boards", boards);
        return "main";
    }

    @GetMapping("/board/{boardId}")
    public String readDetail(@PathVariable Long boardId, Model model) {
        Board readBoard = boardService.readDetail(boardId);
        model.addAttribute("board", readBoard);
        return "board_detail";
    }
    // =============================== READ ===============================

    // =============================== CREATE ===============================
    @GetMapping("/create")
    public String createBoard() {
        return "board_form";
    }

    @PostMapping("/create")
    public String createBoard(@Validated @ModelAttribute Board board, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "board_form";
        }

        Board createBoard = boardService.create(board);
        model.addAttribute("createBoard", createBoard);

        return "redirect:/board/" + createBoard.getId();
    }
    // =============================== CREATE ===============================

    // =============================== UPDATE ===============================
    @PostMapping("/verify/{boardId}")
    @ResponseBody
    public Map<String, Object> verifyPassword(@PathVariable Long boardId, @RequestBody Map<String, String> payload) {
        Board board = boardService.readDetail(boardId);
        String inputPassword = payload.get("password");
        boolean isMatch = board.getPassword().equals(inputPassword);

        Map<String, Object> response = new HashMap<>();
        response.put("match", isMatch);

        return response;
    }

    @GetMapping("/update/{boardId}")
    public String updateBoard(@PathVariable Long boardId, Model model) {
        Board board = boardService.readDetail(boardId);
        model.addAttribute("board", board);
        return "board_update";
    }

    @PostMapping("/update/{boardId}")
    public String updateBoard(@PathVariable Long boardId,
                              @ModelAttribute BoardUpdateDto dto) {
        // 변경 감지로 update
        Board board = boardService.update(boardId, dto);

        return "redirect:/board/" + boardId;
    }
    // =============================== UPDATE ===============================


}
