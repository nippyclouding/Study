package springDataJpa.study.member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
