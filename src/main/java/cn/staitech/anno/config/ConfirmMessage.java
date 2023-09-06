/*
package cn.staitech.anno.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class ConfirmMessage implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
    
    private final RabbitTemplate rabbitTemplate;
    
    */
/*
     * Autowired可以省略，基于构造函数注入的主要优点是可以将需要注入的字段声明为final， 使得它们会在类实例化期间被初始化，这对于所需的依赖项很方便。
     *//*

    
    @Autowired
    public ConfirmMessage(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    
    
    */
/*
     * PostConstruct该注解被用来修饰一个非静态的void()方法。
     * 被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次。PostConstruct在构造函数之后执行，init（）方法之前执行。
     * 通常我们会是在Spring框架中使用到@PostConstruct注解 该注解的方法在整个Bean初始化中的执行顺序： Constructor(构造方法) -> @Autowired(依赖注入) ->
     * PostConstruct(注释的方法)
     *//*

    
    @SuppressWarnings("checkstyle:CommentsIndentation")
    @PostConstruct
    private void initRabbitTemplate() {
        //        将2个实现类配置到 RabbitTemplate对象中
        this.rabbitTemplate.setConfirmCallback(this);
        this.rabbitTemplate.setReturnsCallback(this);
    }
    
    */
/**
     * 异步监听 消息是否到达 exchange .
     *
     * @param correlationData 包含消息的唯一标识的对象
     * @param ack             true 标识 ack，false 标识 nack
     * @param cause           nack 的原因
     *//*

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info("消息正常到达交换机");
        } else {
            log.info("消息没有到达交换机，原因为：" + cause);
            //发送失败，存入redis，发送重试
        }
    }
    
    */
/**
     * 异步监听 消息是否到达 queue 触发回调的条件有两个：1.消息已经到达了 exchange 2.消息无法到达 queue (比如 exchange 找不到跟 routingKey 对应的 queue)
     *
     * @param returnedMessage 报错信息
     *//*

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.error("-------returnedMessage" + returnedMessage.toString());
    }
}

*/
