package zsh.demos.rabbitmq.aop;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Order(2)
@Aspect
@Component
public class AutoLoggerAspect {

	private final Logger logger = LoggerFactory.getLogger(AutoLoggerAspect.class);
	
	@Pointcut("@annotation(zsh.demos.rabbitmq.aop.AutoLog)")
	public void log() {}

	@Before(value="log()")
	public void doBeforeController(JoinPoint joinPoint) {

//		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//		Method method = signature.getMethod();
//		AutoLog action = method.getAnnotation(AutoLog.class);
//		System.out.println("action Name " + action.value()); 
		
		CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        System.out.println("First parameter's name: " + codeSignature.getParameterNames()[0]);
        System.out.println("First argument's value: " + joinPoint.getArgs()[0]);
			
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP_METHOD : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
	}

	@After("log()")
	public void doAfter(JoinPoint joinPoint) {
		System.out.println("@Pointcut -> log() -> @@After");
	}

	@AfterReturning(pointcut = "log()", returning="returns")
	public void doAfterController(JoinPoint joinPoint, Object returns) {
		System.out.println("@Pointcut -> log() -> @AfterReturning");
		System.out.println("retValue is:" + returns);
	}
}
