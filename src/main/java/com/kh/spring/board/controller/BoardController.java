package com.kh.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.kh.spring.board.model.exception.BoardException;
import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;
import com.kh.spring.board.model.vo.Reply;
import com.kh.spring.common.Pagination;
import com.kh.spring.member.model.exception.MemberException;
import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;

@Controller
public class BoardController {

	@Autowired
	private MemberService mService; //= new MemberServiceImpl();
	// 뒤에는 계속해서 영향을 받아 이름이 바뀌는데 그건 옳지 않다 뒤에 지워줌 
   @Autowired
   private BoardService bService;
   

	@Autowired
	private BCryptPasswordEncoder bcrypt;
   
   
   
   @RequestMapping("blist.bo")
   public ModelAndView boardList(@RequestParam(value = "page", required = false) Integer page , ModelAndView mv) {//String값으로 받아주되 파싱하는 귀찮음을 덜어주기위해 Integer page로 받아준다!!
      //게시글 리스트를 보려면 -> 페이징 처리
      
      int currentPage = 1;
      //파라미터로 받아온 페이지번호가 존재하면 받아서 교체
      if(page != null) {
         currentPage = page;
      }
      //리스트카운트 받아오기
      int listCount = bService.getListCount();
      
      //페이지네이션 만들기
      PageInfo pi = Pagination.getPageInfo(currentPage, listCount);
      
      ArrayList<Board> list = bService.getBoardList(pi);
      
      if(list != null ) {
         mv.addObject("list", list);
         mv.addObject("pi", pi);
         mv.setViewName("boardListView");
      } else {
         throw new BoardException("게시글 전체 조회에 실패하였습니다.");
      }
      return mv;
   }
   
   
   @RequestMapping("binsertView.bo")
   public String boardInsertForm() {
	   return "boardInsertForm";
   }
   
   @RequestMapping("binsert.bo")
   public String insertBoard(@ModelAttribute Board b, @RequestParam("uploadFile") MultipartFile uploadFile, HttpServletRequest request) {
	   System.out.println(b);
	   System.out.println(uploadFile);
	   // MultipartFile[field="uploadFile", filename=, contentType=application/octet-stream, size=0] null인데 객체가 출력됨. 
	   if(uploadFile != null && !uploadFile.isEmpty()) {// null 이 없는 상황은 없어서 != null은 꼭 해주는게 좋음. 연결에 대한 문제가 생겼을때 업로드 파일에대한 값이 널이 들어오면 null포인트 잇겝션이 발생되니 미연에 방지하는용도
		  String renameFileName = saveFile(uploadFile, request);//httpservletrequest가 맞다.
		  
		  b.setOriginalFileName(uploadFile.getOriginalFilename());
		  b.setRenameFileName(renameFileName);
	   }
	   
	   int result = bService.insertBoard(b);
	   
	   if(result > 0) {
		   return "redirect:blist.bo";
	   }else {
		   throw new BoardException("게시글 등록에 실패하였씁니다,");
	   }
   }
   
   public String saveFile(MultipartFile file, HttpServletRequest request) {
	   // 사용자 정의 메소드 
	   // 파일저장 디비에 저장소가 따로있다.
	   String root = request.getSession().getServletContext().getRealPath("resources");
	   String savePath = root + "\\buploadFiles";
	   
	   File folder = new File(savePath);
	   if(!folder.exists()) {
		   folder.mkdirs();
	   }
	   
	   SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	   String originFileName = file.getOriginalFilename();// 왜? 확장자까지 뽑아내기 위해 미리만들어놓음
	   String renameFileName = sdf.format(new Date(System.currentTimeMillis())) + originFileName.substring(originFileName.lastIndexOf("."));
	   
	   String renamePath = folder + "\\" + renameFileName;
	   
	   try {
		   file.transferTo(new File(renamePath));// 저장소에 새로운 사진이름으로 저장 . 
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	   
	   return renameFileName;
   }
   
   
   @RequestMapping("bdetail.bo")
   public ModelAndView selectBoardDetail(@RequestParam("bId") int bId , @RequestParam("page") int page, ModelAndView mv) {
	   
	   Board b = bService.selectBoardDetail(bId);
	   
	   if(b != null) {
		   mv.addObject("b",b).addObject("page",page).setViewName("boardDetailView");
       }else {
      	   throw new MemberException("게시글 상세보기에 실패하였습니다.");
       }
	   return mv;
   }
   
   
   // 게시글 수정
   @RequestMapping("bupView.bo")
   public String boardUpdateForm(@ModelAttribute Board b, @RequestParam("page") int page, Model model) {
	   model.addAttribute("board", b).addAttribute("page",page);
      return "boardUpdateForm";
   }
   
   @RequestMapping("bupdate.bo")
   public String updateBoard(@ModelAttribute Board b, @RequestParam("page") int page, @RequestParam("reloadFile") MultipartFile reloadFile, HttpServletRequest request, Model model) {
	  
	   if(reloadFile != null && !reloadFile.isEmpty()) {
		   if(b.getRenameFileName() != null) {
			   deleteFile(b.getRenameFileName(), request);
		   }
		   
		   String renameFileName = saveFile(reloadFile, request);
		   b.setOriginalFileName(reloadFile.getOriginalFilename());
		   b.setRenameFileName(renameFileName);
	   }
	   
	   int result = bService.updateBoard(b);
	   if(result > 0) {
		  //return "redirect:bdetail.bo?bId=" + b.getBoardId() + "&page=" + page;
		   model.addAttribute("bId", b.getBoardId());
		   model.addAttribute("page",page);
		   return "redirect:bdetail.bo";
	   }else {
		   throw new BoardException("게시글 수정에 실패하였습니다.");
	   }
   }
   
   public void deleteFile(String fileName, HttpServletRequest request) {
       String root = request.getSession().getServletContext().getRealPath("resources");
       String savePath = root + "\\buploadFiles";
       
       File f = new File(savePath + "\\" + fileName);
       if(f.exists()) {
          f.delete();// 선생님 DB까지 굳이 갈 필요가 없는거죠??? 전 DB에 가는줄 알았는데..  DB는 안가고 파일만삭제 DB는 업데이트로 값 변경 

       }
    }
   
   
   @RequestMapping("bdelete.bo")
   public String deleteBoard(@RequestParam("bId") int bId, @RequestParam("renameFileName") String renameFileName, HttpServletRequest request) {
	   if(renameFileName.equals("")) {
		   deleteFile(renameFileName, request);
	   }
	   
	   int result = bService.deleteBoard(bId);
	   
	   if(result > 0) {
		   return "redirect:blist.bo";
	   }else {
		   throw new BoardException("게시글 삭제에 실패하였습니다.");
	   }
	   
   }
   
   //댓글 insert 
   // 댓글 저장에 성공했으면 success 반환, 실패했으면 에러 발생: 댓글 등록에 실패하였습니다. 
   
   @RequestMapping("addReply.bo")
   @ResponseBody   // 뷰리졸브가 success.jsp로 인식할 수 있기에 데이터라고 인지시켜준다. 
   public String addReply(@ModelAttribute Reply r, HttpSession session) {
      // 성공 시 success반환하기에 반환값 void아닌 String
      String id = ((Member)session.getAttribute("loginUser")).getId();
      
      r.setReplyWriter(id);
      
      int result = bService.insertReply(r);
      
      if(result > 0) {
         return "success";
      } else {
         throw new BoardException("댓글 등로게 실패하였습니다.");
      }
   }
   
//   @RequestMapping(value="rList.bo", produces="application/json; charset=UTF-8")
//   @ResponseBody
//   public String getReplyList(@RequestParam("bId") int bId) {
   @RequestMapping("rList.bo")
   public void getReplyList(@RequestParam("bId") int bId, HttpServletResponse response) {
	   ArrayList<Reply> list = bService.selectReplyList(bId);
	   
//	   JSONArray jArr = new JSONArray();
//	   for(Reply r : list) {
//		   JSONObject job = new JSONObject();   
//		   job.put("replyId",  r.getReplyId());
//		   job.put("replyContent",  r.getReplyContent());
//		   job.put("replyWriter",  r.getReplyWriter());
//		   job.put("nickName",  r.getNickName());
//		   job.put("refBoardId",  r.getRefBoardId());
//		   job.put("replyCreateDate",  r.getReplyCreateDate());
//		   job.put("replyModifyDate",  r.getReplyModifyDate());
//		   job.put("replyStatus", r.getReplyStatus());
//		   
//		   jArr.put(job);
//	   }
//	   return jArr.toString();
	   
	   response.setContentType("application/json; charset=UTF-8");
	   Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
	   try {
		   gson.toJson(list, response.getWriter());
	    } catch (JsonIOException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
   	}
   
   
   @RequestMapping(value="topList.bo", produces="application/json; charset=UTF-8")
   @ResponseBody
   public String topList() {
	   ArrayList<Board> list = bService.topList();
	   JSONArray jArr = new JSONArray();
	   for(Board b : list) {
		   JSONObject job = new JSONObject();   
		   job.put("boardId",  b.getBoardId());
		   job.put("boardTitle",  b.getBoardTitle());
		   job.put("boardContent",  b.getBoardContent());
		   job.put("nickName",  b.getNickName());
		   job.put("boardModifyDate",  b.getBoardModifyDate());
		   job.put("boardCount",  b.getBoardCount());
		   job.put("originalFileName",  b.getOriginalFileName());
		   
		   jArr.put(job);
	   }
	   return jArr.toString();
	   
   }
}

