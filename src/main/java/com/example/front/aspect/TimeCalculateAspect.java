package com.example.front.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class TimeCalculateAspect {
    @Around("@annotation(com.example.front.annotation.CalculateTime)")
    public Object calculateTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        log.info("AI서버와 통신하는데 걸린 시간 : {}ms", elapsedTime);

        return result;
    }
}
