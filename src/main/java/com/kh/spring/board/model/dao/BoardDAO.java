package com.kh.spring.board.model.dao;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;
import com.kh.spring.board.model.vo.Reply;

@Repository("bDAO")
public class BoardDAO {

	public int getListCount(SqlSessionTemplate sqlSession) {
		
		return sqlSession.selectOne("boardMapper.getListCount");
	}

	public ArrayList<Board> getBoardList(SqlSessionTemplate sqlSession, PageInfo pi) {
		int offset = (pi.getCurrentPage() -1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		
		return (ArrayList)sqlSession.selectList("boardMapper.getBoardList", null, rowBounds);
	}

	public int insertBoard(SqlSessionTemplate sqlSession, Board b) {
		return sqlSession.insert("boardMapper.insertBoard", b);
	}

	public int updateCount(SqlSessionTemplate sqlSession, int bId) {
		return sqlSession.update("boardMapper.updateCount", bId);
	}
	
	public Board selectBoardDetail(SqlSessionTemplate sqlSession, int bId) {
		return sqlSession.selectOne("boardMapper.selectBoardDetail", bId);
	}

	public int updateBoard(SqlSessionTemplate sqlSession, Board b) {
		
		return sqlSession.update("boardMapper.updateBoard", b);
	}

	public int deleteBoard(SqlSessionTemplate sqlSession, int bId) {
		return sqlSession.update("boardMapper.deleteBoard", bId);
	}

	public int insertReply(SqlSessionTemplate sqlSession, Reply r) {
		return sqlSession.insert("boardMapper.insertReply", r);
	}

	public ArrayList<Reply> selectReplyList(SqlSessionTemplate sqlSession, int bId) {
		return (ArrayList)sqlSession.selectList("boardMapper.selectReplyList", bId);
	}

	public ArrayList<Board> topList(SqlSessionTemplate sqlSession) {
		return (ArrayList)sqlSession.selectList("boardMapper.topList");
	}

	
}
