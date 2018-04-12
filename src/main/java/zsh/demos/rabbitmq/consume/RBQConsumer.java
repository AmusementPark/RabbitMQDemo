package zsh.demos.rabbitmq.consume;

import org.springframework.stereotype.Component;

@Component
//@RabbitListener(queues=RabbitMQConf.Q1)
public class RBQConsumer {
	
//	@RabbitHandler
	public void process(String message) {
		System.out.println("RBQConsumer Received <" + message + ">");
	}
}
