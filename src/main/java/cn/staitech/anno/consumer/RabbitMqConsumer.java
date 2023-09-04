package cn.staitech.anno.consumer;

import java.io.IOException;
import javax.annotation.Resource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;
import cn.hutool.json.JSONUtil;
import cn.staitech.anno.domain.vo.specialImageAnno.in.AlgorithmAnnIn;
import cn.staitech.anno.service.SpecialImageAnnoService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RabbitMqConsumer {

	@Resource
	private SpecialImageAnnoService specialImageAnnoService;




	/**
	 * 全脏器切图通知
	 *
	 * @param message
	 * @param channel
	 * @throws IOException
	 */
	/*@RabbitListener(queues = "slide.annotation.slideViscer.queue")
//   @RabbitListener(queues = "organ_callback")
    public void callBackSlideViscer(Message message, Channel channel,List<CallBackAnnAddIn>  list) throws IOException {
    	String sc = JSONUtil.toJsonStr(list);
    	log.info("专题-算法切图数据333333:"+sc);
//    	specialImageAnnoService.callBackSlideViscer(list);
    	// 消息的标识，false只确认当前一个消息收到，true确认所有consumer获得的消息
    	channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    	log.info("专题-算法切图处理 - 队列 {} 消费成功", "special.thumbnail.queue");
    }*/



	/**
	 * 全脏器切图回调
	 *
	 * @param message
	 * @param channel
	 * @throws IOException
	 */
	@RabbitListener(queues = "slide.annotation.organ.queue")
	public void callBackAnnoResult(Message message, Channel channel) throws IOException {
		String jsonStr = new String(message.getBody());
		log.info("全脏器切图回调-从 队列中收到的消息： {}",jsonStr);
		AlgorithmAnnIn  algorithmAnnIn = JSONUtil.toBean(jsonStr, AlgorithmAnnIn.class);
		specialImageAnnoService.callBackAnnoResult(algorithmAnnIn);
		// 消息的标识，false只确认当前一个消息收到，true确认所有consumer获得的消息
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
	}


}

