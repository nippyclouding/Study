package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceTest {

    /*
    테스트 클래스에 @Transactional 을 사용하면 데이터를 DB에 넣어도 테스트가 끝나는 시점에 rollback 된다.
    그러나 현재 테스트에서 @Transactional 을 사용하면 트랜잭션 전파에 변수가 생기기 때문에 사용 x
    => 테스트 수행 시 DB에 저장이 중복으로 될 수 있기 때문에 username을 모두 다르게 저장 필요
     */

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    LogRepository logRepository;

    // 1. 트랜잭션이 각 리포지토리 영역에 걸려있을 때, 성공 케이스
    /*
    MemberRepository : @Transactional off
    LogRepository : @Transactional on
    MemberService : @Transactional on
     */
    @Test
    void outerTxOff_success() {
        //given
        String userName = "outerTxOff_Success";

        //when
        memberService.joinV1(userName);
        /*
        내부적으로 memberRepository 에서 @Transactional 이 사용된다.
        => 트랜잭션 AOP 가 동작, 트랜잭션 매니저를 통해 트랜잭션 시작
        트랜잭션 매니저는 데이터소스를 통해 커넥션을 하나 획득, 수동 커밋 모드로 변경해서 트랜잭션 시작
        트랜잭션 동기화 매니저에 해당 트랜잭션 커넥션 보관, 신규 트랜잭션 여부 확인 => true 를 status로 리턴

        memberRepository 는 위에서 생성한 커넥션을 트랜잭션 동기화 매니저에서 사용하여 저장하고
        memberRepository 가 정상 응답을 반환했기 때문에 트랜잭션 AOP 가 트랜잭션 매니저에 커밋 요청 => 물리 트랜잭션 커밋
        (신규 트랜잭션 여부, rollbackOnly 모두 확인)

        정상 커밋, 트랜잭션 종료, 안전하게 저장
         */

        //then
        //repository 에서 트랜잭션 적용
        assertTrue(memberRepository.findByName(userName).isPresent());
        assertTrue(logRepository.find(userName).isPresent());
    }

    // 2. 트랜잭션이 각 리포지토리 영역에 걸려있을 때, 로그 리포지토리 실패 케이스
    /*
    MemberRepository : @Transactional off
    LogRepository : @Transactional on
    MemberService : @Transactional on
     */
    @Test
    void outerTxOff_fail() {
        //given
        String username = "로그예외_outerTxOff_fail";

        //when
        assertThatThrownBy(() -> memberService.joinV1(username)).isInstanceOf(RuntimeException.class);

        //then
        assertTrue(memberRepository.findByName(username).isPresent()); // 정상 저장
        assertTrue(logRepository.find(username).isEmpty()); // 예외 발생, 런타임 예외 발생, 롤백 처리

    }

    // 3. 트랜잭션이 각 리포지토리 대신 서비스 계층에 걸려서 한 번에 두 개의 리포지토리를 저장할 때
    /*
    MemberRepository : @Transactional on
    LogRepository : @Transactional off
    MemberService : @Transactional off
     */
    @Test
    void singleTX() {
        //given
        String username = "singleTx";

        //when
        memberService.joinV1(username);

        //then
        assertTrue(memberRepository.findByName(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());

    }

    /*
    MemberRepository : @Transactional on
    LogRepository : @Transactional on
    MemberService : @Transactional on
     */
    // 4. 클라이언트의 여러 요구 사항에 따라 서비스 계층, 리포지토리 계층에 각각 트랜잭션이 필요할 때 - 전파 커밋
    @Test
    void outerTxOn_success() {
        //given
        String username = "outerTxOn_success"; // 정상 로직
        //when
        memberService.joinV1(username);
        // 서비스 계층에서 외부 논리 트랜잭션 1개, memberRepository 계층과 logRepository 계층에서 내부 논리 트랜잭션 각각 1개씩 동작
        // 모든 논리 트랜잭션을 커밋해야 물리 트랜잭션도 커밋, 하나라도 롤백되면 물리 트랜잭션도 롤백
        //then
        assertTrue(memberRepository.findByName(username).isPresent());
        assertTrue(memberRepository.findByName(username).isPresent());
    }

    /*
    MemberRepository : @Transactional on
    LogRepository : @Transactional on
    MemberService : @Transactional on
     */
    @Test
    void outerTxOn_fail() {
        //given
        String username = "로그예외_outerTxOn_fail";
        //when
        assertThatThrownBy(() -> memberService.joinV1(username)).isInstanceOf(RuntimeException.class);
        // 런타임 예외 발생, 롤백
        // 내부 logRepository.save 가 예외를 던지며 물리 롤백을 호출하지는 않는다 => rollbackOnly 설정
        // 외부 논리 트랜잭션인 memberService 단계에서 실제 커넥션에 물리 롤백 호출

        //then
        assertTrue(memberRepository.findByName(username).isEmpty());
        assertTrue(logRepository.find(username).isEmpty());
    }

    /*
    MemberRepository : @Transactional on
    LogRepository : @Transactional on
    MemberService : @Transactional on
     */
    @Test
    void recoverException_fail() {
        //given
        String username = "로그예외_recoverException_fail";
        //when
        assertThatThrownBy(()->memberService.joinV2(username)).isInstanceOf(UnexpectedRollbackException.class);
        /*
        내부 논리 트랜잭션이 rollback only 를 적용 => 실제 물리 트랜잭션도 롤백된다, UnexpectedRollbackException 발생
        service 계층은 예외를 잡고 정상 로직 commit 을 반환하지만 커밋 호출 때 rollback Only를 확인하여 롤백한다.
        실무에서 UnexpectedRollbackException 발생 시 해당 상황을 생각하기
        rollbackOnly 상황에서 커밋 발생 시 UnexpectedRollbackException 발생
        */

        //then
        assertTrue(memberRepository.findByName(username).isEmpty());
    }

    @Test
    void recoverException_success() {
        //given
        String username = "로그예외_recoverException_success";
        //when
        memberService.joinV2(username);
        //then
        assertTrue(memberRepository.findByName(username).isPresent());
        assertTrue(logRepository.find(username).isEmpty());
    }

    /*
    MemberRepository : @Transactional on
    LogRepository : @Transactional on (propagation = Propagation.REQUIRES_NEW)
    MemberService : @Transactional on
     */
    @Test
    void recoverException_success_REQUIRES_NEW() {
        //given
        String username = "로그예외_recoverException_success";
        //when
        memberService.joinV2(username);
        // Propagation.REQUIRES_NEW
        // 내부 repository 가 기존 트랜잭션에 참여하는 대신 항상 신규 트랜잭션을 생성 => 새로운 물리 커넥션을 가진다.
        // DB 커넥션도 별도로 사용, 물리 트랜잭션 자체가 완전히 분리된다.
        // logRepository 에서 예외 발생 시 해당 물리 커넥션은 롤백, memberService, repository는 정상 커밋
        // 하나의 HTTP 요청에 동시에 2개의 DB 커넥션 사용 => 성능이 중요한 곳에서는 주의해서 사용해야 한다.

        //then
        assertTrue(memberRepository.findByName(username).isPresent());
        assertTrue(logRepository.find(username).isEmpty());
    }
}