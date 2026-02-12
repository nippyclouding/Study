package hello.advanced.trace;

import lombok.Getter;

import java.util.UUID;

@Getter
public class TraceId {
    private String id; // 트랜잭션 id (http 요청 구분 전용)
    private int level; // 요청 깊이 구분

    // 생성자
    public TraceId() {
        this.id = createId();
        this.level = 0;
    }

    // 생성자, createNextId, createPreviousId 에서 사용
    private TraceId(String id, int level) {
        this.id = id;
        this.level = level;
    }

    // 랜덤 id 생성
    private String createId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    // 다음 로그 추적 객체 생성 (id는 같은 트랜잭션이기 때문에 현재 id 그대로 유지)
    public TraceId createNextId() {
        return new TraceId(id, level + 1);
    }

    // 다음 로그 추적 객체 생성 (id는 같은 트랜잭션이기 때문에 현재 id 그대로 유지)
    public TraceId createPreviousId() {
        return new TraceId(id, level - 1);
    }
    // 깊이가 0인 첫 단계인지 확인하는 메서드
    public boolean isFirstLevel() {
        return level == 0;
    }

}
