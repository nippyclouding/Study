package kr.co.project.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private MemberMapper memberMapper;

	@Override
	public MemberVO login(MemberVO vo) {
		
		return memberMapper.login(vo);
	}

	@Override
	public boolean join(MemberVO vo) {
		
		return memberMapper.join(vo) > 0 ? true : false;
	}

	@Override
	public int emailCheck(String email) {
		return memberMapper.emailCheck(email);
	}

}
