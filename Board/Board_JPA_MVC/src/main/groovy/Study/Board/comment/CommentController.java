package Study.Board.comment;

import Study.Board.comment.dtos.CommentSaveReqDto;
import Study.Board.comment.dtos.CommentUpdateReqDto;
import Study.Board.common.PasswordVerifyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // ============= create =============
    @PostMapping("{boardId}")
    public ResponseEntity<?> save(@Validated @RequestBody CommentSaveReqDto dto, BindingResult bindingResult, @PathVariable Long boardId) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        commentService.save(dto, boardId);
        return ResponseEntity.ok().build();
    }

    // ============= update =============
    @PatchMapping("/{commentId}")
    public ResponseEntity<?> update(@Validated @RequestBody CommentUpdateReqDto dto, BindingResult bindingResult, @PathVariable Long commentId) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        commentService.update(commentId, dto);
        return ResponseEntity.ok().build();
    }

    // ============= delete =============
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> delete(@PathVariable Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.ok().build();
    }

    // ============= verify password =============
    @PostMapping("/verify/{commentId}")
    public ResponseEntity<?> verifyPassword(@PathVariable Long commentId, @RequestBody Map<String, String> payload) {
        String inputPassword = payload.get("password");
        boolean isMatch = commentService.verifyPassword(commentId, inputPassword);

        PasswordVerifyResponseDto dto = new PasswordVerifyResponseDto(isMatch);
        return ResponseEntity.ok(dto);
    }
}
