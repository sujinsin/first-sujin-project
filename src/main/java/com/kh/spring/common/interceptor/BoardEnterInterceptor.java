package com.kh.spring.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import com.kh.spring.member.model.vo.Member;

public class BoardEnterInterceptor implements HandlerInterceptor{
	private Logger logger = LoggerFactory.getLogger(BoardEnterInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if(loginUser == null) {
//			session.setAttribute("msg","로그인 후 이용하세요");
//			response.sendRedirect("home.do");
			request.setAttribute("msg","로그인 후 이용하세요");
			request.getRequestDispatcher("WEB-INF/views/home.jsp").forward(request, response);
			
			return false;
		}
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
}
