package Study.Board.comment.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class CommentUpdateReqDto {
    @NotBlank
    private String comment;
}
