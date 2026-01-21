package book.shop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE member SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;
    private String loginId;
    private String loginPassword;
    private String email;
    private String profileImgUrl;

    @Embedded
    private Address address;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private MyPage mypage;

    @OneToMany(mappedBy = "member") // order 클래스의 member 필드에 매핑
    private List<Order> orders = new ArrayList<>();



    public static Member createMember(String name, String loginId, String password) {
        Member member = new Member();
        member.name = name;
        member.loginId = loginId;
        member.loginPassword = password;

        member.setMypage(new MyPage());
        return member;
    }
    private void setMypage(MyPage mypage) {
        this.mypage = mypage;
        mypage.setMember(this);
    }
}
