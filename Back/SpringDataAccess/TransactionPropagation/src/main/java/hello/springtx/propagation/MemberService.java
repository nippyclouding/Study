package hello.springtx.propagation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final LogRepository logRepository;

    @Transactional
    public void joinV1(String userName) {
        Member member = new Member(userName);
        Log logMessage = new Log(userName);

        log.info("== start to call MemberRepository ==");
        memberRepository.save(member);
        log.info("== end to call MemberRepository ==");

        log.info("== start to call LogRepository ==");
        logRepository.save(logMessage);
        log.info("== end to call LogRepository ==");

        // logRepository 에서 예외 발생 시 예외를 잡지 못한다
    }

    @Transactional
    public void joinV2(String userName) {
        Member member = new Member(userName);
        Log logMessage = new Log(userName);

        log.info("== start to call MemberRepository ==");
        memberRepository.save(member);
        log.info("== end to call MemberRepository ==");

        log.info("== start to call LogRepository ==");

        // logRepository 에서 예외 발생 시 예외를 잡는다
        try {
            logRepository.save(logMessage);
        } catch (RuntimeException e) {
            log.info("log 저장에 실패했습니다. logMessage = {}", logMessage);
            log.info("정상 흐름 리턴");
        }
        log.info("== end to call LogRepository ==");
    }
}
