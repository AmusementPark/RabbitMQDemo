package zsh.demos.rabbitmq.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQCallBack {
	
	private static final Logger LOG = LoggerFactory.getLogger(RabbitMQCallBack.class);

	@Bean
	public ConfirmCallback confirmCallBack() {
		return new ConfirmCallback() {
			@Override
			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
				LOG.info("ConfirmCallback: {}, {}, {}", correlationData, ack, cause);
			}
		};
	}
	
	@Bean
	public ReturnCallback returnCallBack() {
		return new ReturnCallback() {	
			@Override
			public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
				LOG.info("ReturnCallback: {}, {}, {}, {}, {}", message, replyCode, replyText, exchange, routingKey);
			}
		};
	}
	
//	@Bean
//	public RecoveryCallback<Void> recoveryCallback() {
//		return new RecoveryCallback<Void>() {		
//			@Override
//			public Void recover(RetryContext context) throws Exception {
//				// TODO Auto-generated method stub
//				LOG.info("RecoveryCallback: {}", context);
//				return null;
//			}
//		};
//	}
	
//	final Consumer consumer = new DefaultConsumer(channel) {
//		@Override
//		public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//			String message = new String(body, "UTF-8");
//			System.out.println(" [x] Received '" + message + "'");
//			try {
//				doWork(message);
//			} finally {
//				//确认收到消息
//				channel.basicAck(envelope.getDeliveryTag(), false);
//			}
//		}
//	};
}
