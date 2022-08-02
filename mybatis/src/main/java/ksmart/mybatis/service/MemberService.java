package ksmart.mybatis.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import ksmart.mybatis.domain.Member;
import ksmart.mybatis.mapper.MemberMapper;

@Service
public class MemberService {
	
	private final MemberMapper memberMapper;
	
	public MemberService(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}

	public List<Member> getMemberList(){
		System.out.println("$$$$$$$$$$$$$$$$$$33333");
		List<Member> memberList = memberMapper.getMemberList();
		System.out.println("$$$$$$$$$$$$$$$$$$00000");
		return memberList;
	}
	
	public List<Member> getMemberList(Map<String, Object> paraMap){
		
		String searchkey = (String)paraMap.get("searchkey");
		
		if("memberId".equals(searchkey)) {
			searchkey = "memberId";
		}else if("memberLevel".equals(searchkey)) {
			searchkey = "memberLevel";
		}else if("memberName".equals(searchkey)) {
			searchkey = "memberName";
		}else {
			searchkey = "memberEmail";
		}
		paraMap.put("searchkey", searchkey);
		System.out.println("searchkey = " + searchkey);
		
		List<Member> memberList = memberMapper.getMemberList(paraMap);
		
		return memberList;
	}
	
	
	public int addMember(Member member) {
		return memberMapper.addMember(member);
	}
	
	public Member getMemberInfoById(String memberId) {
		return memberMapper.getMemberInfoById(memberId);
	}
	
	public int modifyMember(Member member) {
		return memberMapper.modifyMember(member);
	}
	
	public String removeMember(String memberId, String memberPw) {
		
		Member member = memberMapper.getMemberInfoById(memberId);
		
		String result = "회원삭제실패";
		
		boolean check = false;
		if(member != null) {
			if(memberPw.equals(member.getMemberPw())) {
				check = true;
				
				memberMapper.removeMember(memberId);
				result = "회원삭제성공";
			}
		}
		System.out.println("비밀번호 일치여부 : " + check);
		return result;
	}
}
