package cn.staitech.anno.domain.image;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName tb_image
 */
@TableName(value ="tb_image")
@Data
public class Image implements Serializable {
    /**
     * 图像ID
     */
    @TableId(type = IdType.AUTO)
    private Long imageId;

    /**
     * 无扩展名文件名称
     */
    private String fileName;

    /**
     * 图像名称（文件名）
     */
    private String imageName;

    /**
     * 图像的绝对路径
     */
    private String imagePath;

    /**
     * 图像URL地址
     */
    private String imageUrl;

    /**
     * 缩略图URL地址
     */
    private String thumbUrl;

    /**
     * macro图片URL地址
     */
    private String macroUrl;

    /**
     * label图片RUL地址
     */
    private String labelUrl;

    /**
     * 1024缩略图路径（用于缓存、标注缩略图时需要）
     */
    private String cacheUrl;

    /**
     * 原图缩到cache图的倍数
     */
    private String multiple;

    /**
     * 文件格式
     */
    private String format;

    /**
     * 宽度
     */
    private String width;

    /**
     * 高度
     */
    private String height;

    /**
     * 深度
     */
    private String depth;

    /**
     * 大小
     */
    private String size;

    /**
     * 全局大小
     */
    private String globalSize;

    /**
     * 分辨率
     */
    private String resolvingPower;

    /**
     * 每层的切片个数
     */
    private String tileCountList;

    /**
     * 总层数（小于2则失败）
     */
    private Integer levelCount;

    /**
     * 前端总切片个数
     */
    private Integer chunkTotal;

    /**
     * 图片的MD5值
     */
    private String md5;

    /**
     * x轴分辨率
     */
    private String resolutionX;

    /**
     * y轴分辨率
     */
    private String resolutionY;

    /**
     * 原放大倍数
     */
    private Integer sourceLens;

    /**
     * 创建人id
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人id
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 图片（切片）编号
     */
    private String imageCode;

    /**
     * 0上传中、1上传失败、2解析中、3解析失败、4可用 5:不可用
     */
    private Integer status;

    /**
     * 所在主机ID
     */
    private Integer hostId;

    /**
     * 机构ID
     */
    private Long organizationId;

    /**
     * 轮次ID（现默认1到8）
     */
    private Long roundId;

    /**
     * 业务类型（1原始切片（默认）、2预测切片）
     */
    private Integer bizType;

    /**
     * 图像来源(1前端上传，2目录选片，3TCP客户端上传)
     */
    private Integer source;

    /**
     * 总块数
     */
    private Long fuzzyCountChunk;

    /**
     * 专题号
     */
    private String topicName;

    /**
     * 动物号
     */
    private String animalCode;

    /**
     * 蜡块号
     */
    private String waxCode;

    /**
     * 组别号
     */
    private String groupCode;

    /**
     * 性别（0：M；1：F）
     */
    private Integer sexFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}