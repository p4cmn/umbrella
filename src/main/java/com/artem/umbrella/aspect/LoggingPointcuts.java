package com.artem.umbrella.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class LoggingPointcuts {
    @Pointcut("execution(* com.artem.umbrella.controller.*.*(..))")
    public void allMethodsFromControllers() {
    }

    @Pointcut("execution(* com.artem.umbrella.servise.*.*(..))")
    public void allMethodsFromServices() {
    }
}
