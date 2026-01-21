package book.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class MyPage { // 회원 정보 수정, 구매 완료된 내역 조회

    @Id @GeneratedValue
    @JoinColumn(name = "myPage_id")
    private Long id;

    @MapsId // 회원 id가 pk이자 fk
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private String name; // 수정용
    private String profileImgUrl; // 수정용
    private String email; // 조회용

    @Embedded
    private Address address; // 수정용
    private Long totalOrderPrice; // 조회용

    @Enumerated
    private MemberGrade memberGrade; // 조회용, 회원 등급

    @OneToOne (mappedBy = myPage)
    private WishList wishList;


    public void setMember(Member member) {
        this.member = member;
    }

    @OneToMany(mappedBy = "mypage", cascade = CascadeType.ALL)
    private List<WishList> wishLists = new ArrayList<>();
}
