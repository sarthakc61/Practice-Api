package com.api.practice.aoplogging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogExecutionTimeAdvice {

    @Around("@annotation(com.api.practice.aoplogging.LogExecutionTime)")
    public Object applicationLogger(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object object = pjp.proceed();
        long endTime = System.currentTimeMillis();
        log.info("Method name " + pjp.getSignature() + " : " + "time taken for execution : " + (endTime - startTime));
        return object;

    }
}
