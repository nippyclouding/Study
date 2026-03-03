package shinhanproject.sh.ex2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GuestBookDto {
    private String title;
    private String content;
    private String writer;
    private LocalDateTime registerDate;
    private LocalDateTime modulateDate;
}
