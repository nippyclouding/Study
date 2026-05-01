package springDataJpa.study.member;

import jakarta.persistence.*;
import lombok.*;
import springDataJpa.study.team.Team;

@Entity
@Getter @Setter
@NoArgsConstructor
@ToString(of = {"id", "username", "age"})
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this(0, username);
    }

    public Member(int age, String username) {
        this.age = age;
        this.username = username;
    }

    public Member(int age, String username, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) changeTeam(team);
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

    static class MemberDto {
        private Long id;
        private String username;
        public MemberDto(Member member) {
            this.id = member.getId();
            this.username = member.getUsername();
        }
    }
}
