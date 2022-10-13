package com.kh.spring.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4jTest {
	
	private Logger logger = LoggerFactory.getLogger(Log4jTest.class);/* HomeController.class 가 아니라현재 클래스에대한 이름을 넣어줘야함*/

	public static void main(String[] args) {
		new Log4jTest().test();
		
	}
	
	public void test() {
		logger.error("error");
		logger.warn("warn");
		logger.info("info");
		logger.debug("debug");
		logger.trace("trace");
	}

}

