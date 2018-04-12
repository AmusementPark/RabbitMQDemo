package zsh.demos.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
//@EnableScheduling
public class RabbitmqApplication {
	
	public static ConfigurableApplicationContext APPCTX = null;
//	public final static long ONE_Minute =  5 * 1000;
//	
//	@Scheduled(fixedDelay=ONE_Minute)
//    public void fixedDelayJob(){
//        System.out.println(new Date().toString()+" >>fixedDelay执行....");
//        System.out.println(Thread.currentThread().getId());
//    }
	
	public static void main(String[] args) {
		APPCTX = SpringApplication.run(RabbitmqApplication.class, args);
//		System.out.println(Thread.currentThread().getId());
	}
}
