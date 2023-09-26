package cn.staitech.anno.domain.vo.topic;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 切片-专题（原图像）表 tb_topic
 *
 * @author WangFeng
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopicListVO implements Serializable {

    /**
     * 主键id .
     */
    @TableField(value = "topic_id")
    private Long topicId;

    /**
     * 专题名称 .
     */
    @TableField(value = "topic_name")
    private String topicName;
}
