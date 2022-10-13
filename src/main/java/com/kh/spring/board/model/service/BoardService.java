package com.kh.spring.board.model.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;
import com.kh.spring.board.model.vo.Reply;

public interface BoardService {

	int getListCount();

	ArrayList<Board> getBoardList(PageInfo pi);

	int insertBoard(Board b);

	Board selectBoardDetail(int bId);

	int updateBoard(Board b);

	int deleteBoard(int bId);

	int insertReply(Reply r);

	ArrayList<Reply> selectReplyList(int bId);

	ArrayList<Board> topList();

	
}
