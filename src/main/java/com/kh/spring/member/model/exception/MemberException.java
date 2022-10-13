package com.kh.spring.member.model.exception;

public class MemberException extends RuntimeException {
	// 예외처리 안해주려고 언체크드 조상인 runtime익셉션 익스텐즈 해줌 
		   public MemberException() {}
		   public MemberException(String msg) {
		      super(msg);
		   }
}
