package zsh.demos.rabbitmq.conf;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import zsh.demos.rabbitmq.consume.RBQConsumer;
import zsh.demos.rabbitmq.consume.ZCCConsumer;
import zsh.demos.rabbitmq.consume.ZCCManualACKConsumer;

@Configuration
public class RabbitMQConf {

	private static final String TopicExchangeName = "zsh.demos.rabbitmq.topic-exchange";
	private static final String DeadLetterExchangeName = "zsh.demos.rabbitmq.dead-letter-exchange";
	public 	static final String Q1 = "zsh.demos.rabbitmq.queue-1";
	public 	static final String Q2 = "zsh.demos.rabbitmq.queue-2";
	public 	static final String DLQ = "zsh.demos.rabbitmq.dead-letter-q";
	private static final String RoutingKeyQ1 = "zsh.demos.rabbitmq.pattern-1";
	private static final String RoutingKeyQ2 = "zsh.demos.rabbitmq.pattern-2";
	private static final String DeadLetterRoutingKey = "zsh.demos.rabbitmq.partten-dead-letter";
	
	// 死信交换机 & 死信路由
	@SuppressWarnings({"serial"})
	private static final Map<String, Object> DEAD_LETTER_PARAMS = new HashMap<String, Object>() {{
		put("x-dead-letter-exchange",    DeadLetterExchangeName);
		put("x-dead-letter-routing-key", DeadLetterRoutingKey);
	}};
	
	@Autowired private TopicExchange topicExchange;
	@Autowired private DirectExchange deadLetterExchange;
	@Autowired @Qualifier("Q1") private Queue q1;
	@Autowired @Qualifier("Q2") private Queue q2;
	@Autowired @Qualifier("DEAD_LETTER_Q") private Queue dlq;
	@Autowired @Qualifier("RBQConsumer") private MessageListenerAdapter rbqConsumer;
//	@Autowired @Qualifier("ZCCConsumer") private MessageListenerAdapter zccConsumer;

	// 参数1 name ：队列名
	// 参数2 durable ：是否持久化
	// 参数3 exclusive ：仅创建者可以使用的私有队列，断开后自动删除
	// 参数4 autoDelete : 当所有消费客户端连接断开后，是否自动删除队列
	@Bean @Qualifier("Q1")
	public Queue Q1() { return new Queue(Q1, true, false, false, DEAD_LETTER_PARAMS);  /*持久*/}
	@Bean @Qualifier("Q2")
	public Queue Q2() { return new Queue(Q2, true, false, false, DEAD_LETTER_PARAMS);  /*持久*/}
	
	// 注册交换机
	@Bean
	public TopicExchange topicExchange() {
		return new TopicExchange(TopicExchangeName);
	}
	// 死信交换机
	// 队列配置一旦出现变更，需要删除已经存在的队列（重启MQ）
	// ----------------------------------------------------------------------------
	@Bean
	public DirectExchange deadLetterExchange() {
		return new DirectExchange(DeadLetterExchangeName, true, false);
	}
	
	@Bean @Qualifier("DEAD_LETTER_Q")
	public Queue deadLetterQueue() {
		return new Queue(DLQ, true);
	}
	
	@Bean
	public Binding deadLetterBindding() {
		return BindingBuilder.bind(dlq).to(deadLetterExchange).with(DeadLetterRoutingKey);
	}
	// 绑定队列到交换机
	// ----------------------------------------------------------------------------
	@Bean
	public Binding bindingQ1R1() {
		return BindingBuilder.bind(q1).to(topicExchange).with(RoutingKeyQ1);
	}
	@Bean
	public Binding bindingQ2R1() {
		return BindingBuilder.bind(q2).to(topicExchange).with(RoutingKeyQ1);
	}
	// ----------------------------------------------------------------------------
	@Bean
	public Binding bindingQ2R2() {
		return BindingBuilder.bind(q2).to(topicExchange).with(RoutingKeyQ2);
	}
	
	@Bean 
	@Qualifier("Q1Tpl")
//    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) //必须是prototype类型
    public RabbitTemplate q1Tpl(
    			@Autowired ConnectionFactory connectionFactory, 
    			@Autowired ConfirmCallback confirmCallback,
    			@Autowired ReturnCallback returnCallback) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setRoutingKey(RoutingKeyQ1);
        template.setExchange(TopicExchangeName);
        template.setMandatory(true);
        template.setConfirmCallback(confirmCallback);
        template.setReturnCallback(returnCallback);
        return template;
    }
	
	// 定义消息的发送路径。
	@Bean 
	@Qualifier("Q2Tpl")
    public RabbitTemplate q2Tpl(
    			@Autowired ConnectionFactory connectionFactory, 
			@Autowired ConfirmCallback confirmCallback,
			@Autowired ReturnCallback returnCallback) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setRoutingKey(RoutingKeyQ2);
        template.setExchange(TopicExchangeName);
        template.setMandatory(true);
        template.setConfirmCallback(confirmCallback);
        template.setReturnCallback(returnCallback);
        return template;
    }
	

//	@Bean
//	public ConnectionFactory connectionFactory() {
//		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//		connectionFactory.setHost("localhost");
//		connectionFactory.setPort(5672);
//		connectionFactory.setUsername("guest");
//		connectionFactory.setPassword("guest");
//		connectionFactory.setConnectionTimeout(50000);
////		connectionFactory.setVirtualHost("/");
//		// 必须要设置,消息的回掉
//		connectionFactory.setPublisherConfirms(true);
//		return connectionFactory;
//	}
	
	@Bean
	@Qualifier("RBQConsumer")
	public MessageListenerAdapter rbqListenerAdapter(RBQConsumer listener) {
		return new MessageListenerAdapter(listener, "process");
	}
//	@Bean
//	@Qualifier("ZCCConsumer")
	public MessageListenerAdapter zccListenerAdapter(ZCCConsumer listener) {
		return new MessageListenerAdapter(listener, "process");
	}

	// 定义消息处理路径
	@Bean
	public SimpleMessageListenerContainer containerRBQ(ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(Q1/*, Q2*/);		// 消费两个队列，直连交换机模型
		container.setMessageListener(rbqConsumer);
		return container;
	}
	
	@Autowired private ZCCManualACKConsumer zccManualACKConsumer;
	
	@Bean
	public SimpleMessageListenerContainer containerZCC(ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(Q2);
		container.setAcknowledgeMode(AcknowledgeMode.MANUAL);	// 手动模式
//		container.setMessageListener(zccConsumer);
		container.setMessageListener(zccManualACKConsumer);
		return container;
	}
}
