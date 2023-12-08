package cn.staitech.anno.vo.predictionInfo.in;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @ClassName: EyeSlideResult
 * @Description:
 * @author wanglibei
 * @date 2023年11月15日
 * @version V1.0
 */
@Data
public class EyeSlideResult {

//	private Long id;

//	@ApiModelProperty(value = "描述")
//	private String description;

//	@ApiModelProperty(value = "图像ID")
//	private Long imageId;

//	@ApiModelProperty(value = "切片号")
//	private String imageName;
//
//	@ApiModelProperty(value = "组别")
//	private String groupName;
//
//	@ApiModelProperty(value = "性别")
//	private String gender;
//
//	@ApiModelProperty(value = "种属")
//	private String species;
//
//	@ApiModelProperty(value = "品系")
//	private String productSeries;
//
//	@ApiModelProperty(value = "剂量")
//	private String dosage;

//	@ApiModelProperty(value = "实验动物来源")
//	private String animalSource;
//
//	@ApiModelProperty(value = "动物接收周龄")
//	private String receivingWeek;
//
//	@ApiModelProperty(value = "动物给药周期")
//	private String dosingCycle;
//
//	@ApiModelProperty(value = "动物恢复周期")
//	private String recoveryCycle;
//
//	@ApiModelProperty(value = "死亡日期")
//	private String dateOfDeath;
//
//	@ApiModelProperty(value = "移走原因")
//	private String removeReason;

//	@ApiModelProperty(value = "脏器")
//	private String organ;
//
//	@ApiModelProperty(value = "切片状态")
//	private String slideStatus;
//
//	@ApiModelProperty(value = "病变类型1")
//	private String lesionType1;
//
//	@ApiModelProperty(value = "病变程度1")
//	private String lesionDegree1;
//
//	@ApiModelProperty(value = "病变类型2")
//	private String lesionType2;
//
//	@ApiModelProperty(value = "病变程度2")
//	private String lesionDegree2;
//
//	@ApiModelProperty(value = "处理状态，不可用原因共三种，0上传失败（MD5校验不通过），1解析中，2解析失败（不能获得缩略图）（1.0：0上传未合并,1合并且生成缩略图,2传输图像）")
//	private Integer processFlag;

//	@ApiModelProperty(value = "创建人id")
//	private Long createBy;
//
//	@ApiModelProperty(value = "创建时间")
//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//	private Date createTime;
//
//	@ApiModelProperty(value = "更新人id")
//	private Long updateBy;
//
//	@ApiModelProperty(value = "更新时间")
//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//	private Date updateTime;

//	@ApiModelProperty(value = "专题ID")
//	private Long topicId;
//
//	@ApiModelProperty(value = "专题名称")
//	private String topicName;
//
//	@ApiModelProperty(value = "是否可用0不可用1可用")
//	private Integer status;
//
//	@ApiModelProperty(value = "逻辑删除状态（0删除，1未删除）")
//	private Integer deleteFlag;
//
//	@ApiModelProperty(value = "机构ID")
//	private Long organizationId;
//
//	@ApiModelProperty(value = "所在主机ID")
//	private Integer hostId;

//	@ApiModelProperty(value = "缩略图url地址")
//	private String thumbUrl;

//	@ApiModelProperty(value = "reviewRoundId")
//	private Long reviewRoundId;
//
//	@ApiModelProperty(value = "文件格式")
//	private String format;
//
//	@ApiModelProperty(value = "宽度")
//	private String width;
//
//	@ApiModelProperty(value = "高度")
//	private String height;
//
//	@ApiModelProperty(value = "x轴分辨率")
//	private String resolutionX;
//
//	@ApiModelProperty(value = "y轴分辨率")
//	private String resolutionY;
//
//	@ApiModelProperty(value = "原放大倍数")
//	private Integer sourceLens;
	
	@ApiModelProperty(value = "切片ID")
	private Long slideId;
	
	@ApiModelProperty(value = "备注")
	private String remark;
	
	@ApiModelProperty(value = "图像路径")
	private String imagePath;

	@ApiModelProperty(value = "文件夹名称")
	private String folderName;

	@ApiModelProperty(value = "预测缩略图url")
	private String predictionThumbUrl;

	@ApiModelProperty(value = "AI分析状态：0:待分析（初始状态）、1:AI分析中、2:AI分析成功、3:AI分析失败")
	private Integer aiAnalyzed;

//	@ApiModelProperty(value = "碎片状态默认为0校验通过，1校验不通过")
//	private String eyeMent;

//	@ApiModelProperty(value = "文件夹url")
//	private String folderUrl;

}
