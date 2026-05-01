package springDataJpa.study.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {
    @PersistenceContext
    private EntityManager em;

    // 저장
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    // 삭제
    public void delete(Member member) {
        em.remove(member);
    }

    // 리스트 전체 조회
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    // pk로 단건 조회 (데이터가 반드시 있을 경우)
    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    // pk로 단건 조회 (데이터가 없을 가능 성이 있을 경우)
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    // 데이터 수 조회
    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class).getSingleResult();
    }

}
