package server.QueryDSL;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
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
public class QuerydslBasicTest {

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
    void JPQL() {
        // given
        String qlString = "select m from Member m where m.username = :username";

        // when
        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        // then
        Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void queryDsl() {
        // given
        QMember qMember = QMember.member; // 기본 인스턴스 사용 방식, 셀프 조인이 필요하지 않다면 기본 인스턴스 방식을 사용하는 것을 권장
        // QMember qMember = new QMember("m"); // alias 직접 지정 방식

        // when
        Member findMember = queryFactory
                .select(qMember)
                .from(qMember) // selectFrom(qMember) 로 합칠 수도 있다.
                .where(qMember.username.eq("member1")
                        .and(qMember.age.eq(10))) // .and(), .or() 로 메서드 체이닝 가능
                .fetchOne(); // 결과값이 반드시 하나일 때 fetchOne() 사용

        // then
        Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void searchByQueryDsl() {
        QMember q = QMember.member;
        List<Member> members = queryFactory
                .selectFrom(q)
                .where(q.username.eq("member1"), q.age.eq(10))
                .fetch();
        /*
        .where(qMember.username.eq("member1") : where m.username = 'member1', eq : SQL의 = 로 치환
        .where(qMember.username.ne("member1")
        .where(qMember.username.eq("member1").not
         => where m.username != 'member1', ne & eq().not : SQL의 != 로 치환

        .where(qMember.username.isNotNull()) : IS NOT NULL 조건

        .where (qMember.age.in(10, 20))
        .where (qMember.notIn(10, 20))
        .where (age.between(10, 30))

        .where (qMember.age.goe(30)) : age >= 30, greater or equal
        .where (qMember.age.gt(30)) : age > 30, greater then
        .where (qMember.age.loe(30)) : age <= 30, less or equal
        .where (qMember.age.lt(30)) : age < 30, less then

        .where (qMember.username.like("member%")
        .where (qMember.username.contains("member") : LIKE '%member%'
        .where (qMember.username.startWith("member") : LIKE 'member%'
         */
    }

    @Test
    void sortAndPaging() {
        // given
        em.persist(new Member(100, null)); // username = null
        em.persist(new Member(100, "memberA")); // username = "memberA"
        em.persist(new Member(100, "memberB")); // username = "memberB"

        QMember qMember = QMember.member;

        // when
        // select 쿼리 전달 => 쓰기 지연 저장소의 insert가 먼저 flush된 뒤 select 쿼리 동작
        // 정렬
        List<Member> members = queryFactory
                .selectFrom(qMember)
                .where(qMember.age.eq(100))
                .orderBy(qMember.age.desc(), qMember.username.asc().nullsFirst())
                // ORDER BY m.age DESC, m.username ASC
                // .nullsLast() : 회원 이름이 null 이라면 마지막에 출력
                // .nullsFirst() : 회원 이름이 null 이라면 처음에 먼저 출력
                .fetch();
        Assertions.assertThat(members.get(2).getUsername()).isEqualTo("memberB");

        // 페이징
        // 데이터 자체만 조회하는 방식, 실무 권장
        List<Member> memberList = queryFactory
                .selectFrom(qMember)
                .orderBy(qMember.username.desc())
                .offset(1)  // 두 번째 데이터부터 시작 (인덱스 기준 1 = 실제 두 번째 데이터)
                .limit(2)   // 데이터 2개 조회
                .fetch();

        // then
        Assertions.assertThat(memberList.size()).isEqualTo(2);

        // 데이터 전체 개수 조회 쿼리
        long totalCount = queryFactory
                .select(qMember.count())
                .from(qMember)
                .where(qMember.age.eq(100))
                .fetchOne();
        // then
        Assertions.assertThat(totalCount).isEqualTo(3);

        // 데이터 & 데이터 전체 개수 함께 조회, 매 번 count 쿼리 비효율적으로 동작 => 권장하지 X
        QueryResults<Member> queryResults = queryFactory
                .selectFrom(qMember)
                .orderBy(qMember.username.desc())
                .offset(1)
                .limit(2)
                .fetchResults();
        // then
        Assertions.assertThat(queryResults.getResults().size()).isEqualTo(2);
    }

    @Test
    void aggregationAndGroupBy() throws Exception {
        QMember qMember = QMember.member;
        QTeam qTeam = QTeam.team;

        List<Tuple> aggregationResult = queryFactory
                .select(qMember.count(), // count 쿼리에서도 사용
                        qMember.age.sum(),
                        qMember.age.avg(),
                        qMember.age.max(),
                        qMember.age.min())
                .from(qMember)
                .fetch();

        // Tuple : 여러 다른 타입의 조회 결과를 하나로 담아내기 위한 QueryDSL 전용 객체, Long, Integer, Double ..
        Tuple tuple = aggregationResult.get(0);
        Assertions.assertThat(tuple.get(qMember.count())).isEqualTo(4);

        List<Tuple> groupByResult = queryFactory
                .select(qTeam.name, qMember.age.avg())
                .from(qMember)
                .join(qMember.team, qTeam)
                .groupBy(qTeam.name)
                .fetch();

        Tuple teamA = groupByResult.get(0);
        Tuple teamB = groupByResult.get(1);

        Assertions.assertThat(teamA.get(qTeam.name)).isEqualTo("teamA");
        Assertions.assertThat(teamB.get(qTeam.name)).isEqualTo("teamB");
    }
}
