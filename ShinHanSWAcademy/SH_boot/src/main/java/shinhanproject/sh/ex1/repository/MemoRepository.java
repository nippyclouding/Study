package shinhanproject.sh.ex1.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import shinhanproject.sh.ex1.entity.Memo;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    // insert
    // update
    // delete
    // select

    // 쿼리메서드
    List<Memo> findByIdBetweenOrderByIdDesc(Long from, Long to);

    // 페이징처리 + 정렬 -> Pageable 객체
    Page<Memo> findByIdBetween(Long from, Long to, Pageable pageable);

    // mno가 10보다 작은 데이터 삭제
    void deleteByIdLessThan(Long num);
}
