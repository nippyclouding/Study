package springDataJpa.study.member;

import jakarta.persistence.Entity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Page<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true) // update 이후 영속성 컨텍스트 초기화
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age); // 조건에 맞는 모든 회원들의 나이를 + 1 처리

    // @Query("select m from Member m left join fetch m.team")
    @EntityGraph(attributePaths = {"team"})  // JPQL을 직접 작성하지 않고 fetch join을 하는 방법, 기본적으로 Left Join Fetch 사용
    List<Member> findMemberFetchJoin();
}
