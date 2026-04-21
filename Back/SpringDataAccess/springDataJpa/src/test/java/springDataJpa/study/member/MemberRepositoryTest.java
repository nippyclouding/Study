package springDataJpa.study.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @PersistenceContext
    EntityManager em;
    @Test
    void findByAge() throws Exception {
        // given
        memberRepository.save(new Member(10, "member1"));
        memberRepository.save(new Member(10, "member2"));
        memberRepository.save(new Member(10, "member3"));
        memberRepository.save(new Member(10, "member4"));
        memberRepository.save(new Member(10, "member5"));

        // when
        // 1. 페이징으로 가져올 조건 설정 : 0번째 인덱스부터 3개, 정렬 조건
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

         // 2. 페이지 조회
        Page<Member> page = memberRepository.findByAge(10, pageRequest); // 페이지 조회

        // 3. 결과값 꺼내기
        List<Member> members = page.getContent();

        // then
        Assertions.assertThat(members.size()).isEqualTo(3); // 페이징으로 조회한 데이터 개수 조회
        Assertions.assertThat(page.getTotalElements()).isEqualTo(5); // 전체 데이터 조회
        Assertions.assertThat(page.getNumber()).isEqualTo(0); // 페이지 번호
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2); //전체 페이지 번호
        Assertions.assertThat(page.isFirst()).isTrue(); // 첫 페이지인지 확인
        Assertions.assertThat(page.hasNext()); //다음 페이지 존재 여부 (현재 1페이지, 다음 2페이지)
    }

    @Test
    void bulkUpdate() throws Exception {
        memberRepository.save(new Member(10, "member1"));
        memberRepository.save(new Member(19, "member2"));
        memberRepository.save(new Member(20, "member3"));
        memberRepository.save(new Member(21, "member4"));
        memberRepository.save(new Member(40, "member5"));

        int resultCount = memberRepository.bulkAgePlus(20);
        // update 전 쓰기지연 저장소의 insert 쿼리들을 flush (jpa 기본 특징)
        // @Modifying(clearAutomatically = true) : update 이후 영속성 컨텍스트 초기화

        Assertions.assertThat(resultCount).isEqualTo(3);
    }

    @Test
    void lock() {
        //given
        Member member = new Member(10, "member1");
        memberRepository.save(member);
        em.flush();
        em.clear();

        //when
        // select for update
        List<Member> members = memberRepository.findLockByUsername("member1");
        Member findMember = members.get(0);

    }
    
    @Test
    void customTest() {
        //given
        List<Member> memberCustom = memberRepository.findMemberCustom();

        //when
        for (Member m : memberCustom) System.out.println("m.getId() = " + m.getId());
        
        //then
        
    }
}