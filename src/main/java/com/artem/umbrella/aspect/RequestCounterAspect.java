package com.artem.umbrella.aspect;

import com.artem.umbrella.service.RequestCounterService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class RequestCounterAspect {

    @Before("com.artem.umbrella.aspect.LoggingPointcuts.allMethodsFromControllers()")
    public void incrementCounter(final JoinPoint joinPoint) {
        RequestCounterService.increment();
        log.info("Increment counter from {}.{}. Counter is {}",
                joinPoint.getSignature().getDeclaringType(),
                joinPoint.getSignature().getName(),
                RequestCounterService.get());
    }
}
