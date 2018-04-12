package zsh.demos.rabbitmq.consume;

import org.springframework.stereotype.Component;

@Component
public class ZCCConsumer {

	public void process(String message) {
		System.out.println("ZCCConsumer Received <" + message + ">");
	}

}
