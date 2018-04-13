package zsh.demos.rabbitmq.conf;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import zsh.demos.rabbitmq.consume.RBQConsumer;
import zsh.demos.rabbitmq.consume.ZCCConsumer;

@Configuration
public class RabbitMQConf {

	private static final String TopicExchangeName = "zsh.demos.rabbitmq.topic-exchange";
	public static final String Q1 = "zsh.demos.rabbitmq.queue-1";
	public static final String Q2 = "zsh.demos.rabbitmq.queue-2";
	private static final String RoutingKeyQ1 = "zsh.demos.rabbitmq.pattern-1";
	private static final String RoutingKeyQ2 = "zsh.demos.rabbitmq.pattern-2";
	
	@Autowired private TopicExchange topicExchange;
	@Autowired @Qualifier("Q1") private Queue q1;
	@Autowired @Qualifier("Q2") private Queue q2;
	@Autowired @Qualifier("RBQConsumer") private MessageListenerAdapter rbqConsumer;
	@Autowired @Qualifier("ZCCConsumer") private MessageListenerAdapter zccConsumer;

	@Bean @Qualifier("Q1")
	public Queue Q1() { return new Queue(Q1, true);  /*持久*/}
	@Bean @Qualifier("Q2")
	public Queue Q2() { return new Queue(Q2, true);  /*持久*/}

	// 注册交换机
	@Bean
	public TopicExchange topicExchange() {
		return new TopicExchange(TopicExchangeName);
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
    public RabbitTemplate q1Tpl(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setRoutingKey(RoutingKeyQ1);
        template.setExchange(TopicExchangeName);
        template.setMandatory(true);
        return template;
    }
	
	// 定义消息的发送路径。
	@Bean 
	@Qualifier("Q2Tpl")
    public RabbitTemplate q2Tpl(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setRoutingKey(RoutingKeyQ2);
        template.setExchange(TopicExchangeName);
        template.setMandatory(true);
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
	@Bean
	@Qualifier("ZCCConsumer")
	public MessageListenerAdapter zccListenerAdapter(ZCCConsumer listener) {
		return new MessageListenerAdapter(listener, "process");
	}

	// 定义消息处理路径
	@Bean
	public SimpleMessageListenerContainer containerRBQ(ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(Q1, Q2);		// 消费两个队列，直连交换机模型
		container.setMessageListener(rbqConsumer);
		return container;
	}
	
	@Bean
	public SimpleMessageListenerContainer containerZCC(ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(Q2);
		container.setMessageListener(zccConsumer);
		return container;
	}

}
