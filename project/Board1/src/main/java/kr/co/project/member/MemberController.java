package kr.co.project.member;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;

	// 로그인폼
	@GetMapping("/member/login")
	public void login() {
		
	}
	
	// 로그인처리
	@PostMapping("/member/login")
	public String login(HttpSession sess, MemberVO vo, Model model) {
		MemberVO memberVO = memberService.login(vo);
		if (memberVO == null) {
			model.addAttribute("msg", "아이디 비밀번호가 올바르지 않습니다.");
			model.addAttribute("cmd", "back");
			return "common/return";
		} else {
			sess.setAttribute("loginSess", memberVO);
			return "redirect:/";
		}
	}
	
	// 가입폼
	@GetMapping("/member/join")
	public void join() {
		
	}
	
	// 가입처리
	@PostMapping("/member/join")
	public String join(MemberVO vo, Model model) {
		boolean r = memberService.join(vo);
		if (r) {
			model.addAttribute("msg", "정상적으로 회원가입되었습니다.");
			model.addAttribute("cmd", "move");
			model.addAttribute("url", "login");
		} else {
			model.addAttribute("msg", "회원가입오류");
			model.addAttribute("cmd", "back");
		}
		return "common/return";
	}
	
	@GetMapping("/member/emailCheck")
	@ResponseBody
	public int emailCheck(@RequestParam String email) {
		return memberService.emailCheck(email);
	}
}







