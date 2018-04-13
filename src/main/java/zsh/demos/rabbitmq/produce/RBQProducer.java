package zsh.demos.rabbitmq.produce;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class RBQProducer {
	
	@Autowired 
	@Qualifier("Q1Tpl")
    private RabbitTemplate rabbitTemplate;

    public void produce(String content) {
        String context = "Hello, RBQProducer! - "+content;
        rabbitTemplate.convertAndSend(context);
    }
}
