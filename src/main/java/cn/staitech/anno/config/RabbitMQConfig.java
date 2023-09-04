package cn.staitech.anno.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * . 当没有这个队列的时候会自动创建
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@Configuration
public class RabbitMQConfig {

	@Value("${spring.rabbitmq.host}")
	private String address;
	@Value("${spring.rabbitmq.username}")
	private String username;
	@Value("${spring.rabbitmq.password}")
	private String password;
	@Value("${spring.rabbitmq.virtual-host}")
	private String virtualhost;

	//队列 起名：websocketDirectQueue
	@SuppressWarnings("checkstyle:MethodName")
	@Bean
	public Queue websocketDirectQueue() {
		// durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
		// exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
		// autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
		//一般设置一下队列的持久化就好,其余两个就是默认false
		Map<String, Object> args2 = new HashMap<>();
		args2.put("x-dead-letter-exchange", "dead.letter.exchange");
		args2.put("x-dead-letter-routing-key", "websocket.dead.letter.direct.routing");
		return new Queue("websocket.direct.queue", true, false, false, args2);
	}

	//Direct交换机 起名：websocketDirectExchange
	@SuppressWarnings({"checkstyle:MethodName", "checkstyle:WhitespaceAfter"})
	@Bean
	DirectExchange annoDirectExchange() {
		Map<String, Object> args = new HashMap<>();
		// 绑定备份交换机
		args.put("alternate-exchange", "websocket.backup.exchange");
		return new DirectExchange("anno.direct.exchange", true, false, args);
	}

	//绑定  将队列和交换机绑定, 并设置用于匹配键：websocketDirectRouting
	@Bean
	Binding bindingDirect() {
		return BindingBuilder.bind(websocketDirectQueue()).to(annoDirectExchange()).with("websocket.direct.routing");
	}


	// 定义websocket死信队列
	@SuppressWarnings("checkstyle:MethodName")
	@Bean
	public Queue websocketDeadLetterQueue() {
		return new Queue("websocket.dead.letter.queue", true, false, false);
	}

	// 定义死信交换机
	@SuppressWarnings("checkstyle:MethodName")
	@Bean("deadLetterExchange")
	DirectExchange deadLetterExchange() {
		return new DirectExchange("dead.letter.exchange", true, false);
	}

	// 死信交换机绑定队列
	@SuppressWarnings("checkstyle:MethodName")
	@Bean
	Binding websocketDeadLetterDirectRouting() {
		return BindingBuilder.bind(websocketDeadLetterQueue()).to(deadLetterExchange())
				.with("websocket.dead.letter.direct.routing");
	}


	//备份队列
	@SuppressWarnings("checkstyle:MethodName")
	@Bean
	public Queue websocketBackupQueue() {
		return new Queue("websocket.backup.queue", true);
	}

	// 定义备份交换机
	@SuppressWarnings("checkstyle:MethodName")
	@Bean
	public FanoutExchange websocketBackupExchange() {
		return new FanoutExchange("websocket.backup.exchange");
	}

	// 备份交换机绑定队列
	@SuppressWarnings("checkstyle:MethodName")
	@Bean
	Binding websocketBackupQueueBindingBackupExchange() {
		return BindingBuilder.bind(websocketBackupQueue()).to(websocketBackupExchange());
	}

	// 定义slideAnnotation队列
	@SuppressWarnings("checkstyle:MethodName")
	//    Map<String, Object> map = new HashMap<>();
	//    //设置过期时间为10s
	//        map.put("x-message-ttl",10000);
	//        return new Queue("ttlQueue",true,false,false,map);
	@Bean
	public Queue slideAnnotationResultQueue() {
		return new Queue("slide.annotation.result.queue", true, false, false);
	}


	//绑定  将队列和交换机绑定, 并设置用于匹配键：SlideAnnotationResultRouting
	@SuppressWarnings("checkstyle:MethodName")
	@Bean
	Binding slideAnnotationResultBinding() {
		return BindingBuilder.bind(slideAnnotationResultQueue()).to(annoDirectExchange())
				.with("slide.annotation.result.routing");
	}
	//算法切全脏器
	@Bean
	public Queue slideViscerQueue() {
		return new Queue("slide.annotation.slideViscer.queue", true, false, false);
	}
	//算法切全脏器
	@Bean
	Binding slideViscerBinding() {
		return BindingBuilder.bind(slideViscerQueue()).to(annoDirectExchange())
				.with("slide.annotation.slideViscer.routing");
	}
	
	@Bean
	public Queue annSpecialImageQueue() {
		return new Queue("special.anno.image", true, false, false);
	}
	@Bean
	Binding annSpecialImageBinding() {
		return BindingBuilder.bind(annSpecialImageQueue()).to(annoDirectExchange())
				.with("special.anno.image");
	}
	
	
	@Bean
	public Queue slideSpecialImageQueue() {
		return new Queue("special.slice.image", true, false, false);
	}
	@Bean
	Binding slideSpecialImageBinding() {
		return BindingBuilder.bind(slideSpecialImageQueue()).to(annoDirectExchange())
				.with("special.slice.image");
	}
	
	
	@Bean
	public Queue annpecialImageQueue() {
		return new Queue("slide.annotation.organ.queue", true, false, false);
	}
	//标注回调队列声明
	/*@Bean
	Binding annoSpecialImageBinding() {
		BindingBuilder.bind(annpecialImageQueue()).
		return BindingBuilder.bind(annpecialImageQueue());
//		return BindingBuilder.bind(annpecialImageQueue()).to(annoDirectExchange())
//				.with("special.slice.image");
	}*/
	
	// 使用Jackson2JsonMessageConverter 。在发送消息时，它会先将自定义的消息类序列化成json格式，再转成byte构造 Message
	@Bean
	public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	// 消息转换器 使用Jackson 2 JSON库实现消息与JSON格式之间相互转换
	@Bean
	public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
		return new MappingJackson2MessageConverter();
	}


	@Bean
	public ConnectionFactory connectionFactory(){
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setAddresses(address);
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
//		connectionFactory.setVirtualHost(virtualhost);
		return connectionFactory;
	}

	@Bean
	public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
		RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
		rabbitAdmin.setAutoStartup(true);
		return rabbitAdmin;
	}

}





