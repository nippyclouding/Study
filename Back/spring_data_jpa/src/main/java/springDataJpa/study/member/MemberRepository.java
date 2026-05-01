package springDataJpa.study.member;

import jakarta.persistence.Entity;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Page<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true) // update 이후 영속성 컨텍스트 초기화
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age); // 조건에 맞는 모든 회원들의 나이를 + 1 처리

    // @Query("select m from Member m left join fetch m.team")
    @EntityGraph(attributePaths = {"team"})  // JPQL을 직접 작성하지 않고 fetch join을 하는 방법, 기본적으로 Left Join Fetch 사용
    List<Member> findAll();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String name);

    // 조회용으로만 쿼리 전달 => 수정하지 않을 것이기에 스냅샷을 생성하지 않도록 hibernate 에게 의사 전달
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);
}
