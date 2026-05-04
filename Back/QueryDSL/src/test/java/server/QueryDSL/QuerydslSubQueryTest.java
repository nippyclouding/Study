package server.QueryDSL;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import server.QueryDSL.entity.Member;
import server.QueryDSL.entity.QMember;
import server.QueryDSL.entity.QTeam;
import server.QueryDSL.entity.Team;

import java.util.List;

@SpringBootTest
@Transactional
public class QuerydslSubQueryTest {
    @Autowired
    EntityManager em;
    JPAQueryFactory queryFactory;

    @BeforeEach
    void before() {
        queryFactory = new JPAQueryFactory(em);

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

        // 쓰기 지연 저장소에 insert 쿼리 저장 (team A, B, member 1, 2, 3, 4), flush
    }

    // 서브 쿼리 : JPAExpressions 사용
    @Test
    void subQuery() throws Exception {
        QMember qMember = QMember.member;
        QTeam qTeam = QTeam.team;

        QMember qMemberSub = new QMember("memberSub");

        // age 가 가장 많은 회원 조회
        // select m.* from Member m where m.age = (select Max(age) from Member)
        List<Member> result = queryFactory
                .selectFrom(qMember)
                .where(qMember.age.eq(  // qMember.age.goe( ... ) 도 가능,  goe : great or equal >=
                        JPAExpressions
                        .select(qMemberSub.age.max())
                        .from(qMemberSub))
                        // 서브 쿼리로 나이가 가장 많은 회원의 나이 조회
                )
                .fetch();

        Assertions.assertThat(result).extracting("age").containsExactly(40);
    }

    // 서브 쿼리 : IN 절 사용
    @Test
    void subQuery_IN() throws Exception {
        QMember qMember = QMember.member;
        QTeam qTeam = QTeam.team;

        QMember qMemberSub = new QMember("memberSub");

        // age 가 가장 많은 회원 조회
        // select m.* from Member m where m.age in (select age from Member where age > 10)
        List<Member> result = queryFactory
                .selectFrom(qMember)
                .where(qMember.age.in(  // in 조건절 - 서브쿼리에서 10보다 큰 나이를 모두 조회
                                JPAExpressions
                                    .select(qMemberSub.age)
                                    .from(qMemberSub)
                                    .where(qMemberSub.age.gt(10))
                ))
                .fetch();

        Assertions.assertThat(result).extracting("age").containsExactly(20, 30, 40);
    }

    // select 절에 사용하는 서브쿼리
    @Test
    void subQuery_Select() throws Exception {
        QMember qMember = QMember.member;
        QTeam qTeam = QTeam.team;

        QMember qMemberSub = new QMember("memberSub");

       // select m.username, (select avg(age) from Member) from Member m
        List<Tuple> result = queryFactory
                .select(qMember.username, JPAExpressions.select(qMemberSub.age.avg()).from(qMemberSub))
                .from(qMember)
                .fetch();

        for (Tuple t : result) {
            System.out.println("username = " + t.get(qMember.username));
            System.out.println("age = " + t.get(JPAExpressions.select(qMemberSub.age.avg()).from(qMemberSub)));
        }
    }
}
