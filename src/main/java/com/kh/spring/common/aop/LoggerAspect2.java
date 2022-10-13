package com.kh.spring.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
@Component
@Aspect
public class LoggerAspect2 {
   private Logger logger = LoggerFactory.getLogger(LoggerAspect2.class);
   
   //@Pointcut("excution(* com.kh.spring..*(..))")
   //public void pcForAll() {}
   

//   public void loggerAdvice(JoinPoint joinPoint) { // Before용
   //	  @Around("pcForAll")
   	  @Around("execution(* com.kh.spring..*(..))")
      public Object loggerAdvice(ProceedingJoinPoint joinPoint) throws Throwable { // Around용
      Signature signature = joinPoint.getSignature();
      logger.debug("signature : " + signature);
      
      String type = signature.getDeclaringTypeName();   // 메소드가 있는 클래스 풀네임
      String methodName = signature.getName();   // 타깃 객체 메소드
      logger.debug("type : " + type);
      logger.debug("methodName : " + methodName);
      
      String componentName = null;
      if(type.indexOf("Controller") > -1) {
         componentName = "Controller  \t: ";
      } else if (type.indexOf("Service") > -1) {
         componentName = "ServiceImpl  \t: ";
      } else if (type.indexOf("DAO") > -1) {
         componentName = "DAO  \t\t: ";
      }
      
      logger.debug("[Before] " + componentName + type + "." + methodName + "()");
      
      Object obj = null; // object 반환값이 있다.
//      try {
//         obj = joinPoint.proceed(); //진행시켜
//      } catch (Throwable e) {
//         e.printStackTrace();
//      }   
      
      obj = joinPoint.proceed();
      
      logger.debug("[After] " + componentName + type + "." + methodName + "()");
      
      return obj; // 위 반환값을 void에서 Object로 바꿔준다.
      
   }
}