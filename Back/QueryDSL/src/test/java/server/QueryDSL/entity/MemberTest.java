package server.QueryDSL.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Commit
class MemberTest {
    @Autowired // @PersistenceContext
    EntityManager em;



    @Test
    void testTeamAndMember() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member(10, "member1", teamA);
        Member member2 = new Member(20, "member2", teamA);
        Member member3 = new Member(30, "member3", teamB);
        Member member4 = new Member(40, "member4", teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        // 쓰기 지연 저장소에 insert 쿼리 저장 (team A, B, member 1, 2, 3, 4)

        em.flush(); // insert 쿼리 실행
        em.clear(); // 영속성 컨텍스트 비우기

        // DB에서 조회
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

        for (Member m : members)
            System.out.println("member : " + m + " -> member.team : " + m.getTeam());
    }



}