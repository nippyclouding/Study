package book.shop.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 다음 지도 api로 가져오기
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Address {
    private String sigungu; // 성남시 분당구, 시/군/구 이름
    private String address; // 경기 성남시 분당구 판교역로 166, 기본 주소
    private String zonecode; // 13529, 우편 번호
}
