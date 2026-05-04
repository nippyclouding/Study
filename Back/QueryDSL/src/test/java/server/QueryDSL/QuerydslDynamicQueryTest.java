package server.QueryDSL;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
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
import server.QueryDSL.entity.Team;

import java.util.List;

@SpringBootTest
@Transactional
public class QuerydslDynamicQueryTest {
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

    @Test
    void 동적쿼리_BooleanBuilder() throws Exception {
        QMember qMember = QMember.member;

        // select 조건 (condition)
        String usernameCond = "member1";
        int ageCond = 10;

        List<Member> result = searchMemberByBooleanBuilder(usernameCond, ageCond, qMember); // member1, 10인 회원 조회
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMemberByBooleanBuilder(String usernameCond, Integer ageCond, QMember qMember) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (usernameCond != null) booleanBuilder.and(qMember.username.eq(usernameCond)); // and qMember.username = usernameCond 조건 추가
        if (ageCond != null) booleanBuilder.and(qMember.age.eq(ageCond)); // and qMember.age = ageCond 조건 추가

        return queryFactory.selectFrom(qMember)
                .where(booleanBuilder)
                .fetch();
    }

    @Test
    void 동적쿼리_where() throws Exception {
        QMember qMember = QMember.member;

        // select 조건 (condition)
        String usernameCond = "member1";
        int ageCond = 10;

        List<Member> result = searchMemberByWhere(usernameCond, ageCond, qMember); // member1, 10인 회원 조회
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMemberByWhere(String usernameCond, int ageCond, QMember qMember) {

        BooleanExpression userNameEq = usernameCond != null ? qMember.username.eq(usernameCond) : null;
        BooleanExpression ageEq = usernameCond != null ? qMember.age.eq(ageCond) : null;

        return queryFactory.selectFrom(qMember)
                .where(userNameEq, ageEq)
                .fetch();

    }
}
