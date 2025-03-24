package com.example.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.example.controller.StudentController.*(..))")
    public void logBeforeMethod(){
        System.out.println("Before method execution");
    }

    @After("execution(* com.example.controller.StudentController.*(..))")
    public void logAfterExecution(){
        System.out.println("After method execution");
    }

    @Around("execution(* com.example.controller.StudentController.*(..))")
    public Object logAroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        System.out.println("Execution time: " + (endTime - startTime) + "ms");
        return result;
    }
}
