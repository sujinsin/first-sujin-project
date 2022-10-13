package com.kh.spring.member.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.kh.spring.member.model.exception.MemberException;
import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;

//@Controller
//public class MemberController {
//	
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login() {
//		System.out.println("로그인처리");
//	}
//}
@SessionAttributes("loginUser")
@Controller
public class MemberController {
	
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	private MemberService mService; //= new MemberServiceImpl();
	// 뒤에는 계속해서 영향을 받아 이름이 바뀌는데 그건 옳지 않다 뒤에 지워줌 
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	 
	/*******************파라미터 전송 받기******************/
//	//1. HttpServletRequest방식 : JSP/Servlet방식
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login(HttpServletRequest request) {
//		String id = request.getParameter("id");
//		String pwd = request.getParameter("pwd");
//		
//		System.out.println("id : " + id);
//		System.out.println("pwd : " + pwd);
//	}
	
	
	//2. @RequestParam방식
	// 스프링에서 좀 더 간단하게 파라미터를 받아올 수 있는 방법 
	// HttpServletRequest와 비슷하게 request 객체를 이용하여 데이터를 전송받으나 원하는 타입으로 자동 형변환 가능. 
	// 문론 그 충분히 변환할 수 있는 상태라면 가능하다는 소리다. 
	// 여러가지 속성이있다 value라는것, defaultValue, required 라는 속성이 존재한다. 
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login(@RequestParam("id") String id, @RequestParam("pwd") String pwd) {
//	public void login(@RequestParam(value="id") String id, @RequestParam(value="pwd") String pwd) {
//	public void login(@RequestParam(value="id", defaultValue="hello") String id, @RequestParam(value="pwd", defaultValue="world") String pwd) {
//	public void login(@RequestParam(value="id", defaultValue="hello") String id, 
//					  @RequestParam(value="pwd", defaultValue="world") String pwd,
//					  @RequestParam(value = "text", required=false, defaultValue="spring") String test) {
//		System.out.println("id : " + id);
//		System.out.println("pwd : " + pwd);
//		System.out.println("test : " + test);
//	}
	
	
	//3. @RequestParam 생략방식 
	//  매개변수 명이 파라미터 명과 동일해야 받아올 수 있다. mapping 을 할 수 있다. 
	// @RequestParam 이 안보이기 때문에 value, defaultValue, required 는 사용 할 수 없다. 
	// 애매한게, 생략할 수 있는데, 리퀘스트 파람이 생략을 한 상태로 실무가서 썼는데, 혼난 수료생이있었다?
	// 그래서, 무조건적으로 생략해서 쓰라고 하기는 힘들다. ?? 
	// 리퀘스트 파람을 생략해도 되니까 썼는데, 트집을 잡고싶은 사수를 만날 수 있기때문에, 생략해서 쓸 수도 있기는 하지만, 이왕이면 써서 연습을해라...
	// 두번째로는 가독성이 좋으니까 쓰는게 좋다. 이변수는 뷰에서부터 받아오는구나 리퀘스트파람없으면 얘는 글냥 매개변수로만 화ㅣㄹ용하는 애구나를 알 수 있게 이왕이면 쓰라고함. 
	// 이왕이면 쓰는것ㄱ을 추천한다. 
	// 지금쓰는 매개변수가 뷰에서 받아오는 파라미터인지 그냥 매개변수에 ㅂ집어넣는건지를 구분하기 위해 써라. 
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login(String id, String pwd ) {
//		System.out.println("id : " + id);
//		System.out.println("pwd : " + pwd);
//	}
	
	// 회원가입부에서 받아올게 많지? -> 그렇게 많은걸 받아올때마다 매개변수를 추가하면 복잡하지? 불편하지? 
	// 그래서 내가 엄청 많은 파라미터가 존재한다하면 이걸 쓰는게 좋다. 
	// 4. @ModelAttribute 방식
	// 요청 파라미터가 많은 경우 객체 타입으로 넘겨 받음. 
	
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login(@ModelAttribute Member m) {
//		System.out.println("m : " + m);
//	}
	
	// 5. @MopdelAttrivute 생략방식
	
	
	
	
	// 1. 모델객체 
	// Servelet에서 사용하던 requestScope와 비슷 = scope는 request
	// 뷰ㅜ에 전달하고 자 하는 데이터를 맵형식 (키,벨류) 로 담을 ㄸ 사용
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public String login(Member m, Model model, HttpSession session) {
//		
//		Member loginMember = mService.login(m);//컨트롤러 바꿔주고,
//		
//		
//		//preix : /WEB-INF/views/member
//		if(loginMember != null) {
//			session.setAttribute("loginUser", loginMember);
//			//return "../home";
//			return "redirect:home.do"; // 샌드리다이렉트 효과를 주고 싶으면 redirect: 이걸 써주면 된다 
//			// 그럼 주소창에 home.do 주소가 찍힘 
//		}else {
//			model.addAttribute("msg", "로그인에 실패하였습니다.");
//			return "../common/errorPage";
//		}
//	}// 유알엘 바꾸고싶으면 다른방법으로 해줘야해 그냥 리턴으로 하면 안됨.
	// 이번엗고 생략은 가능한데 추천하지는 않는다. 
	
	
	// 2. ModelAndView 객체 사용
	 // Model + View --> 데이터와 뷰를 한번에 담는 객체 
	// 반환값이 어떤뷰로 넘어갈지를 반환하는건데, ModelAndView가 반환값을 담고있으니, 반환값을 ModelAndView로 바꿔줘야함. String이었던걸
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public ModelAndView login(Member m, ModelAndView mv, HttpSession session) {// ModelAndView 를 써도 되고, Model을 써도 됨 
//		
//		Member loginMember = mService.login(m);//컨트롤러 바꿔주고,
//		
//		
//		//preix : /WEB-INF/views/member
//		if(loginMember != null) {
//			session.setAttribute("loginUser", loginMember);
//			mv.setViewName("redirect:home.do");
//			//return "../home";
////			return "redirect:home.do"; // 샌드리다이렉트 효과를 주고 싶으면 redirect: 이걸 써주면 된다 
//			// 그럼 주소창에 home.do 주소가 찍힘 
//		}else {
//			mv.addObject("msg", "로그인에 실패하였습니다.");// mv 는 담아주는게, addObject 임
//			mv.setViewName("../common/errorPage");
//		}
//		
//		return mv;
//	}
	
	
//	@RequestMapping("logout.me")
//	public String logout(HttpSession session) { // 기본적인 구조는 String으로 일단 가져감
//		session.invalidate();
//		return "redirect:home.do";
//	}
	
	// + session 에 저장 할 ㄸ @ SessionAttributes tkdyd
	// Model 에 attribute가 추가될 때 자동으로 키 값을 찾아 세션에 등록하는 것 
	
	// 로그인 잠시 주석처리 해줌 
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public String login(Member m, Model model) {// httpsession대신에 sesionattribute 사용할거라 지워줌 
//		
//		Member loginMember = mService.login(m);//컨트롤러 바꿔주고,
//		
//		
//		//preix : /WEB-INF/views/member
//		if(loginMember != null) {
//			model.addAttribute("loginUser", loginMember);
//			return "redirect:home.do"; // 샌드리다이렉트 효과를 주고 싶으면 redirect: 이걸 써주면 된다 
//			// 그럼 주소창에 home.do 주소가 찍힘 
//		}else {
//			model.addAttribute("msg", "로그인에 실패하였습니다.");// mv 는 담아주는게, addObject 임
//			return "../common/errorPage";
//		}
//	}
	
	@RequestMapping("logout.me")
	public String logout(SessionStatus status) { // 기본적인 구조는 String으로 일단 가져감
		status.setComplete();//로 무효화를 시켜줌. 
		return "redirect:home.do";
	}
	
	
	@RequestMapping("enrollView.me")
	public String enrollView() {
		
		if(logger.isDebugEnabled()) {
			logger.debug("회원등록페이지");
		}
		// 디버그일때만 걸리게 함. 
		return "memberJoin";
	}
	
	
   @RequestMapping(value = "minsert.me", method = RequestMethod.POST )
      public String insertMember(@ModelAttribute Member m, @RequestParam("post") String post,
    		  					 @RequestParam("address1") String address1, @RequestParam("address2") String address2) {
	   
         
//         System.out.println(m);
//         System.out.println(post + "/" + address1 + "/" + address2);
//         
         m.setAddress(post + "/" + address1 + "/" + address2);
        
         
         String encPwd = bcrypt.encode(m.getPwd());//원본 비번을 넘기는것
         
         
         m.setPwd(encPwd);
         System.out.println(m);
         int result = mService.insertMember(m);
         
         if(result > 0) {
        	 return "redirect:home.do";
         }else {
        	 throw new MemberException("회원가입에 실패하였습니다.");
         }
      }
	
	@RequestMapping(value="login.me", method=RequestMethod.POST)
	public String login(Member m, Model model) {// httpsession대신에 sesionattribute 사용할거라 지워줌 
		Member loginMember = mService.login(m);//컨트롤러 바꿔주고,
		
		boolean match = bcrypt.matches(m.getPwd(), loginMember.getPwd());
		System.out.println(loginMember.getPwd());
		System.out.println(m.getPwd());
		//preix : /WEB-INF/views/member
		if(match) {
			model.addAttribute("loginUser", loginMember);
			logger.info(loginMember.getId());
			return "redirect:home.do"; // 샌드리다이렉트 효과를 주고 싶으면 redirect: 이걸 써주면 된다 
			// 그럼 주소창에 home.do 주소가 찍힘 
		}else {
			//model.addAttribute("msg", "로그인에 실패하였습니다.");// mv 는 담아주는게, addObject 임
			//return "../common/errorPage";
			throw new MemberException("로그인에 실패하였습니다.");
		}
	}
	
	@RequestMapping("myinfo.me")
	public String myInfo() {
		return "mypage";
	}
	
	@RequestMapping("mupdateView.me")
	public String updateView() {
		return "memberUpdateForm";
	}
	
	@RequestMapping("mupdate.me")
	public String updateMember(@ModelAttribute Member m, @RequestParam("post") String post,
								@RequestParam("address1") String address1, @RequestParam("address2") String address2, Model model) {
		
		m.setAddress(post + "/" + address1 + "/" + address2);
		
		int result = mService.updateMember(m);
		Member loginUser = mService.login(m);
		
		 if(result > 0) {
			 model.addAttribute("loginUser", loginUser);
        	 return "redirect:myinfo.me";
        	 
         }else {
        	 throw new MemberException("회원수정에 실패하였습니다.");
         }
	}
	
	 @RequestMapping("mpwdUpdateView.me")
	   public String pwdUpdateView() {
		   return "memberPwdUpdateForm";
	   }
	   
	   @RequestMapping("mPwdUpdate.me")
	   public String updatePwd(@RequestParam("pwd") String oldPwd, @RequestParam("newPwd1") String newPwd, Model model) {
		      Member m = (Member)model.getAttribute("loginUser");// 로그인한 사람의 정보 
		      
		      // 예전방식으로는 수정 절대 불가는 비크립트 암호화 해서 컨트롤러에서 먼저 비교해줘야해. 현재 비번, 암호화 된거 되지 않은거 비교하는 메소드 배웠지??
		      int result = 0;
		      String encode = null;
		     if( bcrypt.matches(oldPwd, m.getPwd())) {// oldPwd 내가 원래 가지고 있던거
		    	 HashMap<String, String> map  = new HashMap<> ();
		    	 map.put("id",m.getId());
		    	 encode = bcrypt.encode(newPwd);
		    	 map.put("newPwd",encode);
		    	 
		    	 result = mService.updatePwd(map);
		     }
		     
		     if(result > 0) {
		    	//return "redirect:logout.me";
		    	 m.setPwd(bcrypt.encode(newPwd));
		    	 model.addAttribute("loginUser",m);
		    	 return "redirect:myinfo.me";
		     }else {
		    	 throw new MemberException("비밀번호 변경에 실패하였습니다.");
		     }
	   }
	   
	   
	   // 인코딩은 인코드라는게 있다.. 유알엘 인코더.. 디코더라는 클래스가 있는데, 에이작스할때 볼것 한다면 보고 안하면 검색...
	   @RequestMapping("mdelete.me")
	   public String deleteMember(Model model) {
		   String id = ((Member)model.getAttribute("loginUser")).getId();
				   
				   int result = mService.deleteMember(id);
		   
		   if(result > 0) {
			   return "redirect:logout.me";
		   }else {
			   throw new MemberException("회원탈퇴에 실패하였습니다.");
		   }
	   }
	
	   
	   @RequestMapping("dupId.me")
	   public void duplicateId(@RequestParam("id") String id, HttpServletResponse response) {
		   System.out.println(id);
		   
		   int result = mService.checkIdDup(id);
		   
		   try {
			   response.getWriter().print(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		   
	   }
	
}

