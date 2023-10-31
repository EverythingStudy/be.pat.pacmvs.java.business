package cn.staitech.anno.vo.topic;

import cn.staitech.anno.domain.Pager;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 切片-专题（原图像）表 tb_topic
 *
 * @author WangFeng
 */
@Data
public class TopicQueryIn extends Pager implements Serializable {

    /**
     * 专题名称 .
     */
    @ApiModelProperty(value = "专题名称")
    private String topicName;

    /**
     * 项目类型 .
     */
    @ApiModelProperty(value = "项目类型")
    private String projectTypeId;

}
