package com.api.practice.aoplogging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAdvice {

    @Pointcut(value = "execution(* com.api.practice.controllers.*.*(..))")
    public void myPointcut(){

    }

    @Around("myPointcut()")
    public Object applicationLogger(ProceedingJoinPoint pjp) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        String methodName = pjp.getSignature().getName();
        String className = pjp.getTarget().getClass().toString();
        Object[] array = pjp.getArgs();
        log.info("Method name " + methodName + " : " + "Class name " + className + " : " + "arguments" +
                mapper.writeValueAsString(array));
        Object object = pjp.proceed();
        log.info(className + " :" + methodName + "()" + " Response : " + mapper.writeValueAsString(object));

        return object;

    }
}
