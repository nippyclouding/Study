package Study.Board.board;

import Study.Board.board.dtos.BoardReadResDto;
import Study.Board.board.dtos.BoardSaveReqDto;
import Study.Board.board.dtos.BoardUpdateReqDto;
import Study.Board.comment.dtos.CommentReadResDto;
import Study.Board.common.PasswordVerifyResponseDto;
import Study.Board.comment.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    @ExceptionHandler(Exception.class)
    public String handleExceptionElement(Exception e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error"; // 전용 에러 페이지로 이동
    }

    // =============================== READ ===============================
    @GetMapping // 페이징 o
    public String findAll(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<BoardReadResDto> boards = boardService.findAllByPageDto(page);

        model.addAttribute("boardPage", boards);
        return "main";
    }

    @GetMapping("/board/{boardId}")
    public String findById(@PathVariable Long boardId, Model model) {
        BoardReadResDto board = boardService.findByIdDto(boardId);
        List<CommentReadResDto> comments = commentService.findByBoardId(board.getBoardId());

        model.addAttribute("board", board);
        model.addAttribute("comments", comments);

        return "board_detail";
    }

    // =============================== CREATE ===============================
    @GetMapping("/create")
    public String save() {
        return "board_form";
    }

    @PostMapping("/create")
    public String save(@Validated @ModelAttribute BoardSaveReqDto dto, BindingResult bindingResult,
                       RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "board_form";
        }

        BoardReadResDto createBoard = boardService.save(dto);

        // model로 보내면 휘발되기 때문에 redirectAttributes로 보낸다.
        redirectAttributes.addFlashAttribute("createBoard", createBoard);

        return "redirect:/board/" + createBoard.getBoardId();
    }

    // =============================== UPDATE ===============================

    @GetMapping("/update/{boardId}")
    public String update(@PathVariable Long boardId, Model model) {
        BoardReadResDto board = boardService.findByIdDto(boardId);
        model.addAttribute("board", board);
        return "board_update";
    }

    @PostMapping("/update/{boardId}")
    public String update(@PathVariable Long boardId,
                              @Validated @ModelAttribute BoardUpdateReqDto dto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/update/" + boardId;
        }

        // 변경 감지로 update
        boardService.update(boardId, dto);

        return "redirect:/board/" + boardId;
    }

    // =============================== DELETE ===============================
    @PostMapping("/delete/{boardId}")
    public String delete(@PathVariable Long boardId) {
        boardService.delete(boardId);
        return "redirect:/";
    }

    // ============= verify password =============
    @PostMapping("/verify/{boardId}")
    @ResponseBody
    public ResponseEntity<?> verifyPassword(@PathVariable Long boardId, @RequestBody Map<String, String> payload) {

        String inputPassword = payload.get("password");
        boolean isMatch = boardService.verifyPassword(boardId, inputPassword);

        PasswordVerifyResponseDto dto = new PasswordVerifyResponseDto(isMatch);

        return ResponseEntity.ok(dto);
    }
}
