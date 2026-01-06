package kr.co.project.member;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
	MemberVO login(MemberVO vo);
	int join(MemberVO vo);
	Integer emailCheck(String email);
}
