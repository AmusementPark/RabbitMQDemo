package zsh.demos.rabbitmq.produce;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ZCCProducer {

	@Autowired 
	@Qualifier("Q2Tpl")
    private RabbitTemplate rabbitTemplate;

    public void produce() {
    		String context = "Hello, ZCCProducer!";
        rabbitTemplate.convertAndSend(context);
    }
}
