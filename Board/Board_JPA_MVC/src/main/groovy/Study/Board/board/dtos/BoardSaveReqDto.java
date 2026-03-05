package Study.Board.board.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardSaveReqDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private String password;
}
