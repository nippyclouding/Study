package shinhanproject.sh.ex2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shinhanproject.sh.ex2.dto.GuestBookDto;
import shinhanproject.sh.ex2.repository.GuestBookRepository;

@RequiredArgsConstructor
@Service
public class GuestBookServiceImpl implements GuestBookService{

    private final GuestBookRepository guestBookRepository;

    @Override
    public Long register(GuestBookDto dto) {
        return guestBookRepository.save(dtoToEntity(dto)).getId();
    }
}
