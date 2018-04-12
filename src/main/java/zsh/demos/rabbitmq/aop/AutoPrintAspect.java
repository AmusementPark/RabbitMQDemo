package zsh.demos.rabbitmq.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Aspect
@Component
public class AutoPrintAspect {
	
	@Pointcut("@annotation(zsh.demos.rabbitmq.aop.AutoLog)")
	public void log() {}

	@Before(value="log()")
	public void doBeforeController(JoinPoint joinPoint) {
		CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        System.out.println("First parameter's name: " + codeSignature.getParameterNames()[0]);
        System.out.println("First argument's value: " + joinPoint.getArgs()[0]);
	}

	@After("log()")
	public void doAfter(JoinPoint joinPoint) {
		
	}

	@AfterReturning(pointcut = "log()", returning="returns")
	public void doAfterController(JoinPoint joinPoint, Object returns) {
		
	}
}
