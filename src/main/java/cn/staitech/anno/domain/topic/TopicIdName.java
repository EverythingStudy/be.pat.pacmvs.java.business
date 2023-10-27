package cn.staitech.anno.domain.topic;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 切片-专题（原图像）表 tb_topic
 *
 * @author: wangfeng
 * @create: 2023-09-15 16:12:40
 * @Description: 专题列表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopicIdName implements Serializable {
    /**
     * 专题ID
     */
    @ApiModelProperty(value = "专题ID")
    private Long topicId;

    /**
     * 专题名称
     */
    @ApiModelProperty(value = "专题名称")
    private String topicName;
}
