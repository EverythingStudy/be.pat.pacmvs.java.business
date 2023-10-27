package cn.staitech.anno.domain.topic.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;


/**
 * 切片-专题（原图像）表 tb_topic
 *
 * @author WangFeng
 */
@Data
public class TopicInsert implements Serializable {

    /**
     * 专题名称 .
     */
    @Size(min = 1, max = 100, message = "{ImageTopicBatchIdsVO.topicName.length}")
    @ApiModelProperty(value = "专题名称")
    private String topicName;
}
