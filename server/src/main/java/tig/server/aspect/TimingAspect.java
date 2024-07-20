package tig.server.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimingAspect {

    @Around("execution(* tig.server.club.service.ClubService.*(..))")
    public Object timeMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long timeTaken = System.currentTimeMillis() - startTime;

        String methodName = joinPoint.getSignature().getName();
        String fullSignature = joinPoint.getSignature().toString();
        String args = fullSignature.substring(fullSignature.indexOf("("), fullSignature.indexOf(")") + 1);
        String requiredPart = methodName + args;

//        System.out.println("\n==============================================================================================================");
        System.out.println(" Execution Time | " + requiredPart + " : " + timeTaken + " ms");
//        System.out.println("==============================================================================================================");

        return result;
    }
}
