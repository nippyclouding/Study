package server.QueryDSL;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
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
public class QuerydslJoinTest {
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

    // inner join
    // select m from member m join team t on m.team_id = t.id where t.name = 'teamA';
    @Test
    void innerJoin() throws Exception {
        QMember qMember = QMember.member;
        QTeam qTeam = QTeam.team;

        List<Member> result = queryFactory
                .selectFrom(qMember)
                .join(qMember.team, qTeam) // join (Q 타입 엔티티의 조인 대상 필드, join할 Q 타입 엔티티), 기본적으로 Team 필드는 가지고 오지 않는다 (n + 1 발생 가능)
                // .leftjoin, .rightjoin 사용 시 left, right join 동작
                .where(qTeam.name.eq("teamA"))
                .fetch();

        Assertions.assertThat(result).extracting("username").containsExactly("member1", "member2");
    }

    // 세타 조인 - SELECT m.* FROM member m, team t WHERE m.username = t.name;
    // 세타 조인 : 연관관계가 없는 데이터끼리 특정 조건으로 연관지어 join
    @Test
    void thetaJoin() throws Exception {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        QMember qMember = QMember.member;
        QTeam qTeam = QTeam.team;

        // on 절을 사용하지 않으면 세타 조인 시 외부 조인이 불가능하다.
        // join 조건 : 회원 이름이 팀 이름과 같은 회원 조회
        List<Member> result = queryFactory
                .select(qMember)
                .from(qMember, qTeam) // from 절에 Q 타입 엔티티를 2개 둔다.
                .where(qMember.username.eq(qTeam.name))
                .fetch();

        Assertions.assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }

    // SELECT m.*, t.* FROM member m LEFT OUTER JOIN team t
    // ON m.team_id = t.id AND t.name = 'teamA';
    // left outer join - member은 모두 조회, team 에만 조건
    @Test
    void on_filtering() throws Exception {
        QMember qMember = QMember.member;
        QTeam qTeam = QTeam.team;

        // join 조건 : 회원, 팀을 join 하며 team 이름이 teamA인 팀만 필터링하여 조회, 회원은 전부 조회
        List<Tuple> result = queryFactory
                .select(qMember, qTeam)
                .from(qMember)
                .leftJoin(qMember.team, qTeam)
                .on(qTeam.name.eq("teamA")) // on 절 : join 대상 필터링 가능, 연관 관계가 없는 엔티티에 대해 외부 조인 가능
                .fetch();

        for (Tuple t : result)
            System.out.println("tuple = " + t);
    }

    // SELECT m.*, t.* FROM member m LEFT OUTER JOIN team t
    // ON m.username = t.name
    // left outer join - member은 모두 조회, team 에만 조건
    @Test
    void on_no_relation() throws Exception {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        QMember qMember = QMember.member;
        QTeam qTeam = QTeam.team;

        // join 조건 : 연관관계가 없는 엔티티 외부 조인 (회원의 이름과 팀 이름이 같은 대상 외부 조인)
        // 세타 조인 - on 절을 쓰지 않고 where 절을 쓸 경우 내부 조인만 가능
        // 세타 조인 - 외부 조인이 필요할 경우 on 절 사용
        List<Tuple> result = queryFactory
                .select(qMember, qTeam)
                .from(qMember)
                .leftJoin(qTeam)
                .on(qMember.username.eq(qTeam.name)) // on 절 : 연관 관계가 없는 엔티티에 대해 외부 조인 가능
                .fetch();

        for (Tuple t : result)
            System.out.println("tuple = " + t);
    }

    @Autowired
    EntityManagerFactory emf;

    // Join 시 fetch join 으로 연관관계 필드도 즉시로딩
    @Test
    void fetchJoin() throws Exception {
        QMember qMember = QMember.member;
        QTeam qTeam = QTeam.team;

        em.flush();
        em.clear();
        // 영속성 컨텍스트 먼저 초기화

        Member findMember = queryFactory
                .selectFrom(qMember)
                // .join(qMember.team, qTeam).fetchJoin() : 생략 시 내부 조인, n + 1 지연 로딩
                .join(qMember.team, qTeam).fetchJoin() // join 절 추가 시 fetch join, 즉시 로딩
                .where(qMember.username.eq("member1"))
                .fetchOne();

        boolean isLoaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        Assertions.assertThat(isLoaded).as("fetch join success").isTrue();
    }
}
