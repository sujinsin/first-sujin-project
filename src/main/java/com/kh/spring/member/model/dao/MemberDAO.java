package com.kh.spring.member.model.dao;

import java.util.HashMap;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.spring.member.model.vo.Member;

@Repository("mDAO")
public class MemberDAO {

	public Member login(SqlSessionTemplate sqlSession, Member m) {
		return sqlSession.selectOne("memberMapper.login", m);
	}

	public int insertMember(SqlSessionTemplate sqlSession, Member m) {

		return sqlSession.insert("memberMapper.insertMember", m);
		// 커밋 시점을 잡았었어 원래 0보다 크면~ 스프링에서는 그렇게 안해도 됨. 알아서 다 잡아준다네..
		// aop안에 tx를 쓰는것 같이 쓰기는 하는데 aop 라는 기술 가지고서 트랩잭션 처리함
		// aop랑 로딩? 인코딩?? 여러가지 같이 합쳐서 쓸 수 있다. 
		// 3시 30분쯤 
	}

	public int updateMember(SqlSessionTemplate sqlSession, Member m) {
		return sqlSession.insert("memberMapper.updateMember", m);
	}

	public int updatePwd(SqlSessionTemplate sqlSession, HashMap<String, String> map) {
		return sqlSession.insert("memberMapper.updatePwd", map);
	}

	public int deleteMember(SqlSessionTemplate sqlSession, String id) {
		return sqlSession.update("memberMapper.deleteMember", id);
	}

	public int checkIdDup(SqlSessionTemplate sqlSession, String id) {
		return sqlSession.selectOne("memberMapper.checkIdDup", id);
	}
	
}
