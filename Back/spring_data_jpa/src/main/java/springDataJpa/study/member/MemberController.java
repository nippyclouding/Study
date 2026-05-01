package springDataJpa.study.member;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    /*
    pageable : 사용자의 http 요청 속 페이징 처리와 관련된 데이터를 담아 만든다, 인터페이스
    pageRequest : pageable 인터페이스의 구현체, http 요청을 토대로 스프링 부트가 생성하는 페이징 요청 정보 객체
    page : pageable 페이징 처리 요청 정보를 토대로 만들어내는 페이징 처리 결과물
     */

    @GetMapping("/members")
    public Page<Member> list(Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        return page;
    }

    @GetMapping("/members/pageableDefault")
    public Page<Member.MemberDto> listPageableDefault(
            @PageableDefault(size = 12, sort = "username", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        Page<Member.MemberDto> dto = page.map(Member.MemberDto::new);
        return dto;
    }

    @GetMapping("/members/dto")
    public Page<Member.MemberDto> dtoList(Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        Page<Member.MemberDto> dto = page.map(Member.MemberDto::new);
        return dto;

        // return memberRepository.findAll(pageable).map(MemberDto::new);
    }

    @PostConstruct
    public void setData(){
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member(i, "member" + i));
        }
    }
}
