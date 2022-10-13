package com.kh.spring.member.model.service;

import java.util.HashMap;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.member.model.dao.MemberDAO;
import com.kh.spring.member.model.vo.Member;

@Service("mService")
public class MemberServiceImpl implements MemberService {

	
	@Autowired
	private MemberDAO mDAO;
	
	//뭘써야 객체에있는 주소값을 가져올 수 있는가? 어노테이션 써야지 
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	
	@Override
	public Member login(Member m) {
		return mDAO.login(sqlSession, m);
	}
	// Sqlsession -> 마이바티스 추가해야함. 
	
	@Override
	public int insertMember(Member m) {
		
		return mDAO.insertMember(sqlSession, m);
	}

	@Override
	public int updateMember(Member m) {
		return mDAO.updateMember(sqlSession, m);
	}

	@Override
	public int updatePwd(HashMap<String, String> map) {
		return mDAO.updatePwd(sqlSession, map);
	}

	@Override
	public int deleteMember(String id) {
		return mDAO.deleteMember(sqlSession, id);
	}

	@Override
	public int checkIdDup(String id) {
		return mDAO.checkIdDup(sqlSession, id);
	}
	
}
