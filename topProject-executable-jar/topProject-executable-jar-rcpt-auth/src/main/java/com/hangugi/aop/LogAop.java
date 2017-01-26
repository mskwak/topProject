package com.hangugi.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 	execution(public void set*(..)) 리턴 타입이 void이고, 메서드 이름이 set으로 시작하고, 파라미터가 0개 이상인 메서드 호출
 	execution(* com.hangugi.*.*()) com.hangugi 패키지에 파라미터가 없는 모든 메서드 호출
 	execution(* com.hangugi..*.*(..)) com.hangugi 패키지 및 하위 패키지에 있는 파라미터가 0개 이상인 메서드 호출
 */

@Aspect
public class LogAop {
	private final static Logger logger = LoggerFactory.getLogger(LogAop.class);

	@Around("execution(public * com.hangugi..*.*(..))")
	public Object trace(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		System.out.println("before advice");
		logger.info("before advice");
		Object object = proceedingJoinPoint.proceed();
		System.out.println(object.getClass().getSimpleName());
		logger.info("after advice");
		System.out.println("after advice");

		return object;
	}
}
