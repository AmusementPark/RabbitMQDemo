package zsh.demos.rabbitmq.consume;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

/**
 * 手动ACK
 */
@Component
public class ZCCManualACKConsumer implements ChannelAwareMessageListener {

	private static final Logger LOG = LoggerFactory.getLogger(ZCCManualACKConsumer.class);
	
	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		// TODO Auto-generated method stub
		byte[] body = message.getBody();
        LOG.info("消费端接收到消息 : {}, {}", message.getMessageProperties().getDeliveryTag(), new String(body));
        Thread.sleep(1000);
//        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);	// 没有ack成功
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
	}

}
