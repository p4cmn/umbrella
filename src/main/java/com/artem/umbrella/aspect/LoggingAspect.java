package com.artem.umbrella.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Before("com.artem.umbrella.aspect.LoggingPointcuts.allMethodsFromControllers() "
            + "|| com.artem.umbrella.aspect.LoggingPointcuts.allMethodsFromServices()")
    public void logSignatureOfCalledMethods(final JoinPoint joinPoint) {
        log.info("Method {}.{} with args: {} called",
                joinPoint.getSignature().getDeclaringType(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs())
        );
    }

    @AfterReturning(value = "com.artem.umbrella.aspect.LoggingPointcuts.allMethodsFromControllers() "
            + "|| com.artem.umbrella.aspect.LoggingPointcuts.allMethodsFromServices()",
            returning = "result")
    public void logReturnValueOfExecutedMethods(final JoinPoint joinPoint, final Object result) {
        log.info("Method {}.{} with args: {} returned: {}",
                joinPoint.getSignature().getDeclaringType(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()),
                result
        );
    }

    @AfterThrowing(value = "com.artem.umbrella.aspect.LoggingPointcuts.allMethodsFromServices()",
            throwing = "exception")
    public void logExceptionOfInterruptedMethods(final JoinPoint joinPoint, final Throwable exception) {
        log.error("Method {}.{} with args: {} interrupted. Exception: {}",
                joinPoint.getSignature().getDeclaringType(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()),
                exception.getMessage());
    }
}
