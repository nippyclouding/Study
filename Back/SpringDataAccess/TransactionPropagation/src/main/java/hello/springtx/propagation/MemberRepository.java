package hello.springtx.propagation;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    // @Transactional(propagation = Propagation.REQUIRED) 와 @Transactional은 동일
    // 기존 트랜잭션이 없으면 새로운 트랜잭션을 생성, 기존 트랜잭션이 있다면 참여
    @Transactional
    public void save(Member member) {
        log.info("member save");
        em.persist(member);
    }

    public Optional<Member> findByName(String userName) {
        return em.createQuery("select m from Member m where m.userName =:userName", Member.class)
                .setParameter("userName", userName)
                .getResultList().stream().findAny();
    }
}
