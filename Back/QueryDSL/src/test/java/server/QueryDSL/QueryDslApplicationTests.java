package server.QueryDSL;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import server.QueryDSL.entity.QTestEntity;
import server.QueryDSL.entity.TestEntity;

@SpringBootTest
@Transactional
class QueryDslApplicationTests {

	@Autowired
	EntityManager em;

	// contextLoads : 해당 스프링 부트 프로젝트가 오류 없이 제대로 실행(로딩)되는지를 확인하는 가장 기초적인 테스트
	@Test
	void contextLoads() {

		TestEntity t = new TestEntity();
		em.persist(t); // 쓰기 지연 저장소에 insert 쿼리 저장, 영속화 (1차 캐시에 저장)

		JPAQueryFactory queryFactory = new JPAQueryFactory(em);

		// Q 타입 엔티티는 기존 변수 이름과 동일한 Q 타입 엔티티를 가진다.
		// public static final QTestEntity testEntity = new QTestEntity("testEntity");
		QTestEntity qTestEntity = QTestEntity.testEntity;


		// qTestEntity 를 이용하여 TestEntity 테이블에 있는 데이터를 select (fetchOne() : 단일 조회, fetch() : 모두 조회)
		// select 쿼리가 동작하기 전 먼저 쓰기 지연 저장소의 insert 쿼리가 flush 된 후 select 동작
		TestEntity result = queryFactory
				.selectFrom(qTestEntity)
				.fetchOne();
		// fetch() : 리스트 조회, fetchOne() : 결과가 반드시 하나 (2개 이상이라면 NonUniqueResultException)

		// 하나의 트랜잭션 안에서는 JPA 영속성 컨텍스트에서 동일성(== 비교)을 보장한다. (1차 캐시의 주솟값을 같이 공유)
		Assertions.assertThat(result).isEqualTo(t);
		Assertions.assertThat(result.getId()).isEqualTo(t.getId());
	}
}
