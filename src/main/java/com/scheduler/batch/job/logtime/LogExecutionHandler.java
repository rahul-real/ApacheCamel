package com.scheduler.batch.job.logtime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LogExecutionHandler {
	private static final String VOID = "void";
	
	private class MethodDetails{
		
		Object[] args;
		String methodName;
		String simpleClassName;
		boolean isVoidMethod;
	}
	
	@Around("@annotation(com.scheduler.batch.job.logtime.annotation.LogExecutionTime)")
	public Object log(ProceedingJoinPoint pjp) throws Throwable {
		MethodDetails methodDetails = getMethodDetails(pjp);
		try {
			StopWatch stopWatch = new StopWatch("Execution-Time");
			stopWatch.start();

			Object result = pjp.proceed();
			stopWatch.stop();
			log.info("Time Spend Analysis {} -> {} took: {} secs",methodDetails.simpleClassName,methodDetails.methodName,
					stopWatch.getTotalTimeSeconds());
			return result;
		} catch (Exception e) {
			logAfterError(log,methodDetails.simpleClassName, methodDetails.methodName,methodDetails.args,e);
			throw e;
		}
	}

	/**
	 * @param log2
	 * @param simpleClassName
	 * @param methodName
	 * @param args
	 * @param e
	 */
	private void logAfterError(Logger log2, String simpleClassName, String methodName, Object[] args, Exception e) {
		log2.error("Error while invoking Class: {}, Method: {}, Args: {} {}", simpleClassName,methodName,args,e);
		
	}

	/**
	 * @param pjp
	 * @return
	 */
	private MethodDetails getMethodDetails(ProceedingJoinPoint pjp) {
		MethodDetails methodDetails = new MethodDetails();
		methodDetails.args = pjp.getArgs();
		
		MethodInvocationProceedingJoinPoint mipjp = (MethodInvocationProceedingJoinPoint) pjp;
		MethodSignature signature = (MethodSignature) mipjp.getSignature();
		methodDetails.methodName = signature.getMethod().getName();
		methodDetails.simpleClassName = signature.getMethod().getDeclaringClass().getName();
		methodDetails.isVoidMethod = signature.getReturnType().getName().equals(VOID);
		return methodDetails;
	}

}
