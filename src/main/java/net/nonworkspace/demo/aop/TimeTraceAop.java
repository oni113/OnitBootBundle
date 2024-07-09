package net.nonworkspace.demo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeTraceAop {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Around("execution(* net.nonworkspace..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        logger.debug("START: " + joinPoint.toShortString());

        try {
            return joinPoint.proceed();
        } finally {
            long finishTime = System.currentTimeMillis();
            long timeMs = finishTime - startTime;
            logger.debug("END: " + joinPoint.toShortString() + " " + timeMs + "ms");
        }
    }
}
