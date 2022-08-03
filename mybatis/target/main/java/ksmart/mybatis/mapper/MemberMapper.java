package ksmart.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import ksmart.mybatis.domain.Member;

@Mapper
public interface MemberMapper {

	public List<Member> getMemberList();
	
	public int addMember(Member member);
	
	public Member getMemberInfoById(String memberId);
	
	public int modifyMember(Member member);
	
	public int removeMember(String memberId);
	
	public List<Member> getMemberList(Map<String, Object> paraMap);
}
