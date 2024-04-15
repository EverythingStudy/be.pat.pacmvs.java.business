package cn.staitech.anno.service.impl;

import cn.staitech.anno.config.MapConstant;
import cn.staitech.anno.constant.Container;
import cn.staitech.anno.domain.Image;
import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.domain.SlidePrediction;
import cn.staitech.anno.mapper.ImageMapper;
import cn.staitech.anno.mapper.SlidePredictionMapper;
import cn.staitech.anno.service.ImageService;
import cn.staitech.anno.service.RetryService;
import cn.staitech.anno.service.SlideService;
import cn.staitech.anno.utils.LanguageUtils;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.utils.StatisticListUtils;
import cn.staitech.anno.vo.image.ImageStatus;
import cn.staitech.anno.vo.image.in.*;
import cn.staitech.anno.vo.image.out.ImageListOutVO;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static cn.staitech.common.security.utils.SecurityUtils.isAdmin;

/**
 * 切片列表（原图像）服务层实现
 *
 * @author wangfeng
 * @date 2023/12/20
 */
@Slf4j
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {

	@Resource
	private ImageMapper imageMapper;
	@Resource
	private SlideService slideService;
	@Resource
	private SlidePredictionMapper slidePredictionMapper;
	@Resource
	private RetryService retryService;

	/**
	 * 切片状态列表 .
	 */
	@Override
	public List<ImageStatus> status() {
		List<ImageStatus> list = new ArrayList<>();
		if (LanguageUtils.isEn()) {
			for (Map.Entry<Integer, String> entry : Container.IMAGE_STATUS_MAP_EN.entrySet()) {
				list.add(new ImageStatus(entry.getKey(), entry.getValue()));
			}
		} else {
			for (Map.Entry<Integer, String> entry : Container.IMAGE_STATUS_MAP.entrySet()) {
				list.add(new ImageStatus(entry.getKey(), entry.getValue()));
			}
		}
		return list;
	}

	/**
	 * 切片列表（原图像）
	 *
	 * @param vo
	 * @return
	 */
	@Override
	@SuppressWarnings("checkstyle:MissingJavadocMethod")
	@Transactional(rollbackFor = Exception.class)
	public PageMaster<ImageListOutVO> selectList(ImageListVO vo) throws ExecutionException, InterruptedException {
		Image image = new Image();
		BeanUtils.copyProperties(vo, image);

		SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();

		if (!Objects.equals(sysUser.getUserName(), "admin")) {
			image.setOrganizationId(sysUser.getOrganizationId());
		}
		// 业务类型 1 原始切片 2 预测切片
		Integer bizType = image.getBizType();

		// 异步查询图像列表
		CompletableFuture<PageMaster<Image>> listFuture = CompletableFuture.supplyAsync(() -> {
			// 分页
			PageHelper.startPage(vo.getPageNum(), vo.getPageSize()).setReasonable(true);
			List<Image> list = imageMapper.selectListSlfe(image);
			PageMaster pageMaster = new PageMaster<>(list);
			return pageMaster;
		});

		PageMaster<Image> pageMaster = listFuture.get();
		List<Image> list = pageMaster.getList();

		// response List
		List<ImageListOutVO> respList = new ArrayList<>();

		if (list.size() > 0) {
			// 数据格式化
			for (Image in : list) {
				ImageListOutVO out = new ImageListOutVO();
				BeanUtils.copyProperties(in, out);
				out.setBusinessType(in.getBizType());
				// 提取处理状态文本描述并赋值 - 0上传中、1上传失败、2解析中、3解析失败、4可用
				Integer status = in.getStatus();

				if (LanguageUtils.isEn()) {
					String fileStatus = Container.IMAGE_STATUS_MAP_EN.get(status);
					out.setFileStatus(fileStatus);
					// 评审轮次
					if (bizType.equals(2)) {
						out.setRoundName(MapConstant.getRoundNameEn(in.getRoundId()));
					}
				} else {
					String fileStatus = Container.IMAGE_STATUS_MAP.get(status);
					out.setFileStatus(fileStatus);
					// 评审轮次
					if (bizType.equals(2)) {
						out.setRoundName(MapConstant.getRoundName(in.getRoundId()));
					}
				}

				// 匹配机构名称
				out.setOrganizationName(MapConstant.getOrganizationName(in.getOrganizationId()));

				// 图片类型
				if (bizType == 7) {
					String businessTypeName = out.getFolderId() == 0 ? "BUSINESS_TYPENAME_7_0" : "BUSINESS_TYPENAME_7_1";
					out.setBusinessTypeName(MessageSource.M(businessTypeName));
				}

				Slide slide = new Slide();
				slide.setImageId(out.getImageId());
				// 禁止删除
				out.setDeleState(0);
				if (slideService.selectImageExist(slide).size() > 0) {
					out.setDeleState(1);
				}
				if(imageMapper.selectFrSlideCountByImageId(out.getImageId()) > 0){
					out.setDeleState(1);
				}
				respList.add(out);
			}
		}

		PageMaster<ImageListOutVO> resp = new PageMaster<>(respList);
		resp.setTotal(pageMaster.getTotal());
		resp.setPages(pageMaster.getPages());
		resp.setPageNum(pageMaster.getPageNum());
		resp.setPageSize(pageMaster.getPageSize());
		//清除分页缓存
		PageHelper.clearPage();
		return resp;
	}


	/**
	 * 项目管理-图像列表
	 *
	 * @param vo
	 * @return
	 */
	@Override
	@SuppressWarnings("checkstyle:MissingJavadocMethod")
	@Transactional(rollbackFor = Exception.class)
	public PageMaster<ImageListOutVO> choiceList(ImageTopicVO vo) throws ExecutionException, InterruptedException {
		Image image = new Image();
		BeanUtils.copyProperties(vo, image);

		// 机构ID
		SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();

		if (!isAdmin(sysUser.getUserId())) {
			image.setOrganizationId(sysUser.getOrganizationId());
		}

		// 只查可用状态的 - 0上传中、1上传失败、2解析中、3解析失败、4可用
		image.setStatus(4);
		// 业务类型 1 原始切片 2 预测切片
		Integer bizType = image.getBizType();

		// 异步查询图像列表
		CompletableFuture<PageMaster<Image>> listFuture = CompletableFuture.supplyAsync(() -> {
			// 分页
			PageHelper.startPage(vo.getPageNum(), vo.getPageSize()).setReasonable(true);
			List<Image> list = null;
			// 添加状态：NULL查全部、0未添加、1已添加
			// 1、查询所有状态
			// 2、未选中：image表为主表 not in slide表中的image_id
			// 3、已选中：slide表为主表 join image获取基础数据
			if (vo.getChoiceState() == null) {
				list = imageMapper.selectListSlfe(image);
			} else if (vo.getChoiceState() == 0) {
				list = imageMapper.selectNotChoicedList(image);
			} else if (vo.getChoiceState() == 1) {
				list = imageMapper.selectChoicedList(image);
			}
			PageMaster pageMaster = new PageMaster<>(list);
			return pageMaster;
		});

		PageMaster<Image> pageMaster = listFuture.get();
		List<Image> list = pageMaster.getList();

		// response List
		List<ImageListOutVO> respList = new ArrayList<>();

		if (list.size() > 0) {
			// 数据格式化
			for (Image in : list) {
				ImageListOutVO out = new ImageListOutVO();
				BeanUtils.copyProperties(in, out);

				// 提取处理状态文本描述并赋值
				Integer status = in.getStatus();

				if (LanguageUtils.isEn()) {
					// 可用、不可用状态解析中
					out.setFileStatus(Container.IMAGE_STATUS_MAP_EN.get(status));
					// 评审轮次
					if (bizType.equals(2)) {
						out.setRoundName(MapConstant.getRoundNameEn(in.getRoundId()));
					}
				} else {
					// 可用、不可用状态解析中
					out.setFileStatus(Container.IMAGE_STATUS_MAP.get(status));
					// 评审轮次
					if (bizType.equals(2)) {
						out.setRoundName(MapConstant.getRoundName(in.getRoundId()));
					}
				}

				// 匹配机构名称
				out.setOrganizationName(MapConstant.getOrganizationName(in.getOrganizationId()));

				if (vo.getChoiceState() == null) {
					// 查询选中状态
					Slide slide = new Slide();
					slide.setImageId(out.getImageId());
					if (vo.getReviewRoundId() != null && vo.getReviewRoundId() > 0) {
						slide.setReviewRoundId(vo.getReviewRoundId());
					} else {
						slide.setProjectId(vo.getProjectId());
					}
					// 查询当前项目或评审轮次是否选中此图片
					if (slideService.selectImageExist(slide).size() > 0) {
						out.setChoiceState(1);
					} else {
						out.setChoiceState(0);
					}
				} else if (vo.getChoiceState() == 0) {
					out.setChoiceState(0);
				} else if (vo.getChoiceState() == 1) {
					out.setChoiceState(1);
				}

				respList.add(out);
			}
		}

		PageMaster<ImageListOutVO> resp = new PageMaster<>(respList);
		resp.setTotal(pageMaster.getTotal());
		resp.setPages(pageMaster.getPages());
		resp.setPageNum(pageMaster.getPageNum());
		resp.setPageSize(pageMaster.getPageSize());
		//清除分页缓存
		PageHelper.clearPage();
		return resp;
	}

	/**
	 * 查询单个图像信息
	 *
	 * @param image
	 * @return
	 */
	@Override
	public Image selectById(Long image) {
		return imageMapper.selectById(image);
	}

	/**
	 * 通过图片ID查询切片
	 *
	 * @param imageId
	 * @return
	 */
	@Override
	public Integer selectSlideCountByImageId(Long imageId) {
		return imageMapper.selectSlideCountByImageId(imageId);
	}

	/**
	 * 通过ID批量修改图片状态
	 *
	 * @param imageIdList
	 * @return
	 */
	@Override
	public int updateBatchIds(ImageTopicBatchIdsVO imageIdList) {
		return imageMapper.updateBatchIds(imageIdList);
	}

	/**
	 * 标注组图像列表
	 *
	 * @param image
	 * @return
	 */
	@Override
	public List<Image> selectImageAnnotationList(Image image) {
		return imageMapper.selectImageAnnotationList(image);
	}

	/***
	 * 批量删除图片（物理删除）
	 * 1、若符合删除条件，图像物理删除；
	 * 2、与切片列表有关联的不能删除；
	 * 3、失败的图像：（1）、按路径查询只有一条MYSQL记录可以删除MYSQL数据和图像物理文件；
	 *              （2）、多条的只删除当前的MYSQL记录；
	 * @param ids
	 * @return
	 */
	@Override
	public List<Long> deleteBatchIds(ImageBatchIdsVO ids) throws Exception {
		Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
		// 不可删除的列表
		List<Long> forbidIds = new ArrayList<>();
		for (Long imageId : ids.getImageIdList()) {
			// 1、查询aipre_slide_prediction是否有关系图像
			QueryWrapper<SlidePrediction> slidePredictionQueryWrapper = new QueryWrapper<>();
			slidePredictionQueryWrapper.eq("image_id", imageId);
			List<SlidePrediction> slidePredictionList = slidePredictionMapper.selectList(slidePredictionQueryWrapper);

			// 2、查询切片表中是否包含该切片,已经关联的,使用中的不可删除
			if (slidePredictionList.size() > 0 || imageMapper.selectSlideCountByImageId(imageId) > 0|| imageMapper.selectFrSlideCountByImageId(imageId)>0) {
				forbidIds.add(imageId);
			} else {
				Image image = imageMapper.selectById(imageId);
				String imagePath = image.getImagePath().trim();
				// 若为空串,只删除SQL记录,跳出
				if (StringUtils.isEmpty(imagePath)) {
					imageMapper.deleteById(imageId);
					continue;
				}

				//模糊程度 （0：初始值 1：模糊 2：不模糊）
				int fuzzyLevel = image.getFuzzyLevel();
				String imageUrl = image.getImageUrl();
				// 查询当前机构下相同路径的图像数量
				QueryWrapper<Image> imageQueryWrapper = new QueryWrapper<>();
				imageQueryWrapper.eq("image_path", image.getImagePath().trim());
				imageQueryWrapper.eq("organization_id", organizationId);
				List<Image> imageList = imageMapper.selectList(imageQueryWrapper);
				//TODO 删除SQL记录   如果是清晰的、不用处理，如果是模糊文件，需要把源文件拷贝到模糊文件目录下，与模糊数据组成一对+
				if(fuzzyLevel == 1){
					//先拷贝文件到模糊目录下
					boolean tag = moveImageFile(imageUrl,image);
					if(tag){
						//TODO向振浩的表插入数据
					}
				}
				imageMapper.deleteById(imageId);


				// 如果只有一条记录,删除文件
				if (imageList.size() == 1) {
					removeImageFile(image);
				}

			}
		}
		return forbidIds;
	}

	/**
	 * 关联切片与专题、组织ID，没有专题则新添加
	 *
	 * @param vo
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public int updateById(ImageUpdateVO vo) throws Exception {
		Image image = new Image();
		BeanUtils.copyProperties(vo, image);

		// 获取当前登录用户Id
		Long loginUser = SecurityUtils.getUserId();
		image.setUpdateBy(loginUser);
		return imageMapper.updateById(image);
	}

	/**
	 * 检查是否存在符合条件的记录
	 *
	 * @param image
	 * @return
	 */
	@Override
	public boolean exists(Image image) throws Exception {
		LambdaQueryWrapper<Image> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.select(Image::getImageId);
		queryWrapper.eq(Image::getMd5, image.getMd5());
		queryWrapper.eq(Image::getOrganizationId, image.getOrganizationId());
		queryWrapper.orderByDesc(Image::getImageId);
		queryWrapper.last("limit 1");
		return this.baseMapper.selectOne(queryWrapper) != null;
	}

	/**
	 * 异步删除切片物理文件
	 *
	 * @param image
	 * @throws Exception
	 */
	@Async
	private void removeImageFile(Image image) throws Exception {
		//模糊程度 （0：初始值 1：模糊 2：不模糊）
		int fuzzyLevel = image.getFuzzyLevel();
		String imagePath = image.getImagePath();
		String imageUrl = image.getImageUrl();
		retryService.deleteFileRetry(new File(imagePath));

		if (StringUtils.isNotEmpty(imageUrl)) {
			retryService.deleteFileRetry(new File(imageUrl));
		}

		if (StringUtils.isNotEmpty(image.getMacroUrl())) {
			retryService.deleteFileRetry(new File(image.getMacroUrl().replace("/file/statics", "/home/pat_saas")));
		}
		if (StringUtils.isNotEmpty(image.getLabelUrl())) {
			retryService.deleteFileRetry(new File(image.getLabelUrl().replace("/file/statics", "/home/pat_saas")));
		}
		if(fuzzyLevel != 1){
			if (StringUtils.isNotEmpty(image.getThumbUrl())) {
				retryService.deleteFileRetry(new File(image.getThumbUrl().replace("/file/statics", "/home/pat_saas")));
			}
		}
	}

	private boolean moveImageFile(String imageUrl,Image image){
		boolean moveTag = false;
		// 源SVS文件路径
		Path sourceSVS = Paths.get(imageUrl);
		// 目标路径
		Path targetDirectory = Paths.get("C:/Users/86153/Desktop/医疗PD/0325/");

		try {
			// 确保目标目录存在
			if (!Files.exists(targetDirectory)) {
				Files.createDirectories(targetDirectory);
			}

			// 目标文件完整路径
			Path targetPath = targetDirectory.resolve(sourceSVS.getFileName());

			// 移动文件
			//Path movePath = Files.move(sourceSVS, targetPath, StandardCopyOption.REPLACE_EXISTING);

			boolean success = Files.move(sourceSVS, targetPath, StandardCopyOption.REPLACE_EXISTING)
					.toAbsolutePath()
					.toFile()
					.exists();
			if (success) {
				moveTag = true;
//				System.out.println("SVS file moved successfully.");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error moving SVS file.");
		}finally {
			return moveTag;
		}

	}

	@Override
	public void clarityProcessing(ResultCorrectionIn req) {
		Long imageId = req.getImageId();
		Image image = imageMapper.selectById(imageId);
		//修正状态  1：修正  2：还原
		if(req.getDefinitionStatus() == 1){
			//是否可用0不可用1可用
			image.setStatus(1);
			//模糊程度 （0：初始值 1：模糊 2：不模糊）
			image.setFuzzyLevel(2);
			image.setDefinitionStatus(1);
		}else{
			//是否可用0不可用1可用
			image.setStatus(0);
			//模糊程度 （0：初始值 1：模糊 2：不模糊）
			image.setFuzzyLevel(1);
			image.setDefinitionStatus(req.getDefinitionStatus());
		}
		imageMapper.updateById(image);
		//TODO fr_blur_image更新
	}
	
	 /*public static String geNumber(Long organizationId) {
	        NumberFormat formatter = NumberFormat.getNumberInstance();
	        formatter.setMinimumIntegerDigits(3);
	        formatter.setGroupingUsed(false);
	        return "C" + formatter.format(organizationId) + "/ImageQC/";
	    }*/

}
