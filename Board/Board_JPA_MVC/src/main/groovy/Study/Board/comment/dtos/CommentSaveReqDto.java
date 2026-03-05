package Study.Board.comment.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentSaveReqDto {
    @NotBlank
    private String comment;
    @NotBlank
    private String password;
}
