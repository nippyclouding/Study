package shinhanproject.sh.ex2.service;

import shinhanproject.sh.ex2.domain.GuestBook;
import shinhanproject.sh.ex2.dto.GuestBookDto;

public interface GuestBookService {
    Long register(GuestBookDto dto);

    default GuestBook dtoToEntity(GuestBookDto dto) {
        return GuestBook.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
    }
}
