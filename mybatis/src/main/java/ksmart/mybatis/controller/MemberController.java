package ksmart.mybatis.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ksmart.mybatis.domain.Member;
import ksmart.mybatis.service.MemberService;

@Controller
public class MemberController {
	
	private final MemberService memberService;
	
	public MemberController(MemberService memberService) {
		 this.memberService = memberService;
	}
		
	@RequestMapping(value= "/addMember", method=RequestMethod.GET)
	public String addMember(Model model) {
		
		model.addAttribute("title", "회원목록");
		return "member/addMember";
	}

	@GetMapping("/memberList")
	public String getMemberList(@RequestParam(name = "searchkey", required=false) String searchkey,
					            @RequestParam(name = "searchValue", required=false) String searchValue,
					            Model model) {
		
		System.out.println("======================");
		System.out.println("== ( memberList.html 화면에서 입력받은 searchkey ) :: " + searchkey);
		System.out.println("== ( memberList.html 화면에서 입력받은 searchValue ) :: " + searchValue);
		System.out.println("======================");
		
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("searchkey", searchkey);
		paraMap.put("searchValue", searchValue);
		
		
		List<Member> memberList = memberService.getMemberList(paraMap);
		System.out.println(paraMap);

		
		model.addAttribute("title", "회원목록");
		model.addAttribute("memberList", memberList);
		
		
		return "member/memberList";
	}
	

	
	@PostMapping("/addMember")
	public String addMember(Member member) {
		System.out.println("======================");
		System.out.println("== 커맨드 객체 member : " + member + "==");
		System.out.println("======================");
		
		if(member != null) memberService.addMember(member);
		
		return "redirect:/memberList";
	}
	
	@GetMapping("/modifyMember")
	public String modifyMember(@RequestParam(name="memberId", required=false) String memberId, Model model) {
		System.out.println("=================================");
		System.out.println("==== memberId" + memberId + "====");
		System.out.println("=================================");
		
		Member member = memberService.getMemberInfoById(memberId);
		
		model.addAttribute("member", member);
		
		return "member/modifyMember";
	}
	
	@PostMapping("/modifyMember")
	public String modifyMember(Member member) {
		System.out.println("========================");
		System.out.println("== 화면에서 입력해준 member 회워 : " + member + "============");
		System.out.println("========================");
		
		if(member != null && member.getMemberId() != null) {
			memberService.modifyMember(member);
		}
		
		return "redirect:/memberList";
	}
	
	@GetMapping("/removeMember")
	public String removeMember(@RequestParam(name = "memberId", required=false) String memberId, 
			                   @RequestParam(name = "result", required=false) String result,
			                   Model model) {
		
		System.out.println("(removemember) 화면에서 입력받은 값 :: " + memberId);
		
		model.addAttribute("title", "회원탈퇴");
		model.addAttribute("memberId", memberId);
		
		if(result != null) {
			System.out.println("!@!@!@!@!@!");
			model.addAttribute("result",result);
		}
		
		return "member/removeMember";
	}
	
	@PostMapping("/removeMember")
	public String removeMember(@RequestParam(name = "memberId", required=false) String memberId,
			                   @RequestParam(name = "memberPw", required=false) String memberPw,
			                   RedirectAttributes redirectAttr) {
		System.out.println("================================");
		System.out.println("( removeMember ) 화면에서 입력받은 memberId : " + memberId);
		System.out.println("( removeMember ) 화면에서 입력받은 memberPw : " + memberPw);
		System.out.println("================================");
		
		String result = memberService.removeMember(memberId, memberPw);
		System.out.println(result);
		
		if("회원삭제실패".equals(result)) {
			redirectAttr.addAttribute("memberId", memberId);
			redirectAttr.addAttribute("result", result);
			
			return "redirect:/removeMember";
		}
		
		return "redirect:/memberList";
	}
	
	@GetMapping("/login")
	public String login(Model model,
						@RequestParam(name="result", required=false) String result
						) {
		model.addAttribute("title","로그인");
		
		if(result != null) model.addAttribute("result",result);
		
		return "login/login";
	}
	
	@PostMapping("/login")
	public String loing(@RequestParam(name = "memberId", required=false) String memberId,
			            @RequestParam(name = "memberPw", required=false) String memberPw,
			            HttpSession session, 
			            RedirectAttributes redirectAttr) {
		
		
		if(memberId != null && !"".equals(memberId) &&
		   memberPw != null && !"".equals(memberPw)) {
			
			Member member = memberService.getMemberInfoById(memberId);
			
			System.out.println("==============================");
			System.out.println("==== login : " + memberId + "===");
			System.out.println("==============================");
			
			if(member != null) {
				
				if(memberPw.equals(member.getMemberId())) {
					session.setAttribute("SID", member.getMemberId());
					session.setAttribute("SLEVEL", member.getMemberLevel());
					session.setAttribute("SNAME", member.getMemberName());
					
					return "redirect:/";
				}
			}
		}
		
		redirectAttr.addAttribute("result","등록된 회원 정보가 없습니다.");
		
		return "redirect:/login";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		
		return "redirect:/login";
	}
	

	
}
