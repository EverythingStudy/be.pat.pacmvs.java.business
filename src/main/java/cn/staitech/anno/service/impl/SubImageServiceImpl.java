package cn.staitech.anno.service.impl;

import cn.hutool.json.JSONUtil;
import cn.staitech.anno.domain.Group;
import cn.staitech.anno.domain.SubImage;
import cn.staitech.anno.domain.SysDictData;
import cn.staitech.anno.domain.organization.SysOrganizationAuthorization;
import cn.staitech.anno.domain.special.Special;
import cn.staitech.anno.domain.specialAnnotation.SpecialAnnotation;
import cn.staitech.anno.domain.specilaImage.SpecialImage;
import cn.staitech.anno.domain.vo.image.ImageRelVo;
import cn.staitech.anno.domain.vo.image.SubImageVo;
import cn.staitech.anno.domain.vo.specialSliceImage.SpecialSliceSelectVO;
import cn.staitech.anno.domain.vo.specialSliceImage.SpecialSliceVo;
import cn.staitech.anno.enums.SysDictTypeEnum;
import cn.staitech.anno.mapper.*;
import cn.staitech.anno.service.GetUserInformationService;
import cn.staitech.anno.service.SubImageService;
import cn.staitech.anno.service.SysDictDataService;
import cn.staitech.anno.service.SysUserService;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.constant.CacheConstants;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.utils.StringUtils;
import cn.staitech.common.redis.service.RedisService;
import cn.staitech.common.security.utils.DictUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * @author mugw
 * @version 1.0
 * @description 图像操作
 * @date 2023/5/29 16:33:38
 */
@Slf4j
@Service
public class SubImageServiceImpl extends ServiceImpl<SubImageMapper, SubImage> implements SubImageService {

	@Resource
	private GroupMapper groupMapper;

	@Resource
	private SubImageMapper subImageMapper;

	@Resource
	private SpecialImageMapper specialImageMapper;

	@Resource
	private SpecialAnnotationMapper specialAnnotationMapper;

	@Resource
	private SpecialMapper specialMapper;


	@Autowired
	private RedisService redisService;
	
	@Resource
    private GetUserInformationService getUserInformationService;
	
	@Autowired
	private SysDictDataService sysDictDataService;
	

	/**
	 * 切片图像分页查询
	 * @param params
	 * @return
	 */
	@Override
	public R<PageMaster<SubImageVo>> pageSubImage(Map params) {
		//2023-08-30修改添加在选择切片列表，展示了切片配置列表里的部分切图完成的切片
		//期望   只有全部交付成功的切片才会在选择切片列表里展示，未进行全部交付，则选择切片列表不会有切片
		Long specialId = MapUtils.getLong(params,"specialId");
		if (specialId==null){
			return R.fail("专题id为空");
		}
		Special special = specialMapper.selectById(specialId);
		Long deliveryStatus = special.getDeliveryStatus();
		if (deliveryStatus==null||deliveryStatus==0){
			return R.fail("专题未交付，交付后进行此操作");
		}
		//根据传入groupId查询分组信息
		Group group = groupMapper.selectById(MapUtils.getLong(params,"groupId",0L));
		Page<SubImageVo> page = new Page<>(MapUtils.getInteger(params,"pageNum",1),MapUtils.getInteger(params,"pageSize",10));
		getBaseMapper().pageSubImage(page,params);
		List<SubImageVo> subImageVos = page.getRecords();
		Map<Long, SubImageVo> temp = new HashMap<>();
		//以imageIds作为参数查询图像关联属性
		List<Long> imageIds = new ArrayList<>();
		try {
			if (subImageVos != null && !subImageVos.isEmpty()) {
				for (SubImageVo vo : subImageVos) {
					if(group!=null){
                        vo.setGroup(group);
                        // vo.setGender(group.getGender());
                    }
					imageIds.add(vo.getImageId());
					temp.put(vo.getImageId(),vo);
				}
				//查询出图像关联属下
				List<ImageRelVo> imageRelVos = getBaseMapper().selectImageRelByIds(imageIds);
				if (imageRelVos!=null&&!imageRelVos.isEmpty()){
					for (ImageRelVo vo : imageRelVos){
						//将关联属性设置到图像对象中
						SubImageVo subImageVo = temp.get(vo.getImageId());
						BeanUtils.copyProperties(vo,subImageVo);
					}
				}
			}
		}catch (Exception e){
			log.error("切片图像分页查询异常：{}",e.getMessage());
			throw e;
		}
		//构建分页对象
		PageMaster<SubImageVo> pageMaster = PageMaster.of(subImageVos);
		pageMaster.setTotal(page.getTotal());
		return R.ok(pageMaster);
	}

	/**
	 * 根据分组查询切片
	 * @param params
	 * @return
	 */
	@Override
	public R<List<SubImageVo>> querySubImageByGroup(Map params) {
		Page<SubImageVo> page = new Page<>(1,Integer.MAX_VALUE );
		getBaseMapper().pageSubImage(page,params);
		return R.ok(page.getRecords());
	}

	@Override
	public List<SpecialSliceVo> selectSpecialSliceVo(SpecialSliceSelectVO sisv) {
		List<SpecialSliceVo>  list = specialImageMapper.selectSpecialSliceVoList(sisv);
		Map<String,String> organizationMap = getDictInfo(SysDictTypeEnum.sysvisceraorganization.label());
		if(CollectionUtils.isNotEmpty(list)){
			list.forEach(ssv -> {
				long imageId = ssv.getImageId();
				long specialId = ssv.getSpecialId();
				long sliceBatchNumber = ssv.getSliceBatchNumber();
				//slice_image_status 切图状态 0:未切图 1：生成中 2：切图完成 3：绘制中
				int sliceImageStatus = ssv.getSliceImageStatus();
				List<SubImage> subList = new ArrayList<>();
				if(sliceImageStatus == 1 ||sliceImageStatus == 2){
					//查询小图列表
					Map<String,Object> columnMap = new HashMap<>();
					columnMap.put("parent_image_id", imageId);
					columnMap.put("slice_batch_number", sliceBatchNumber);
					columnMap.put("special_id", specialId);
					columnMap.put("process_flag", 2);
					subList = subImageMapper.selectByMap(columnMap);
				}
				if(CollectionUtils.isNotEmpty(subList)){
					for(SubImage subImage : subList){
						int visceraType = subImage.getVisceraType();
						if(organizationMap.containsKey(visceraType+"")){
							String visceraName = organizationMap.get(visceraType+"");
							subImage.setVisceraName(visceraName);
						}else{
							subImage.setVisceraName("");
						}
					}
				}
				ssv.setSubImageList(subList);
				//增加是否可以被编辑的状态标识 是否可编辑 1:不可以编辑 2：可以编辑
				if(sliceImageStatus == 0 || sliceImageStatus == 3 && null != ssv.getEditBy() && ssv.getEditBy() == SecurityUtils.getUserId()){
					ssv.setEditStatus(2);
				}
			});
		}
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void selectExpireSpecialSlice(SpecialSliceSelectVO sisv) {
		//根据专题id去查询所有的编辑中的人，查询token是否过期，过期的需要将切图状态修改为未切图、编辑人改为空，结果清空
		Map paramMap = new HashMap<>();
		//		切图状态 0:未切图 1：生成中 2：切图完成 3：绘制中
		paramMap.put("sliceImageStatus", 3);
		paramMap.put("specialId", sisv.getBelongSpecialId());
		List<SpecialImage> siList = specialImageMapper.selectSpecialImageListByParm(paramMap);
		if(CollectionUtils.isEmpty(siList)){
			//查询编辑人是否token过期
			List<Long> idsList = new ArrayList<>();
			Map<Long,Integer> loginMap = new HashMap<>();
			siList.forEach(ssv -> {
				String updateToken = ssv.getUpdateByToken();
				if(loginMap.isEmpty()|| null != loginMap && !loginMap.containsKey(ssv.getEditBy())){
					SysUser loginUser = getUserInformationService.selectById(ssv.getEditBy());
					//根据用户id获取token，校验是否过期
					String userNameKey = CacheConstants.LOGIN_TOKEN_KEY+loginUser.getUserName();
					String cacheObject = redisService.getCacheObject(CacheConstants.LOGIN_TOKEN_KEY+userNameKey);
					//token过期
					if (StringUtils.isNull(cacheObject)) {
						idsList.add(ssv.getSpecialImageId());
						// 1 代表过期
						loginMap.put(ssv.getEditBy(),1);
					}else{
						if(org.apache.commons.lang3.StringUtils.isNotEmpty(updateToken)){
							// 2 代表没过期
							if(updateToken.equalsIgnoreCase(cacheObject)){
								loginMap.put(ssv.getEditBy(),2);
							}else{
								idsList.add(ssv.getSpecialImageId());
								// 1 代表过期
								loginMap.put(ssv.getEditBy(),1);
							}
						}else{
							loginMap.put(ssv.getEditBy(),2);
						}
					}
				}else{
					//取出当前人，判断是否过期
					Integer loginStatus = loginMap.get(ssv.getEditBy());
					if(loginStatus  == 1){
						//token过期
						idsList.add(ssv.getSpecialImageId());
					}
				}
			});

			if(CollectionUtils.isNotEmpty(idsList)){
				SpecialImage record = new SpecialImage();
				record.setEditBy(-1l);
				record.setSliceImageStatus(0);
				Long[] idArray = idsList.toArray(new Long[idsList.size()]);
				record.setSpecialImageIds(idArray);
				specialImageMapper.updateByPrimaryKeySelective(record);
				for(Long specialImageId :idsList){
					deleteAnno(specialImageId);
				}
			}
		}
	}
	
	public void deleteAnno(Long specialImageId ){
		//查询专题id下所有的标注数据
		SpecialAnnotation sa = new SpecialAnnotation();
		sa.setSpecialImageId(specialImageId);
		List<SpecialAnnotation>  list = specialAnnotationMapper.selectSpecialAnnotationList(sa);
		if(org.apache.commons.collections.CollectionUtils.isNotEmpty(list)){
			for(SpecialAnnotation an : list){
				//删除标注数据
				specialAnnotationMapper.deleteByPrimaryKey(an.getSliceAnnotationId());
			}
		}
	}
	
	@Override
	public void logOutSpecial(String userName) {
		//根据用户查询用户名称查询正在绘制中的所有数据
		Map paramMap = new HashMap<>();
		//		切图状态 0:未切图 1：生成中 2：切图完成 3：绘制中
		paramMap.put("sliceImageStatus", 3);
		paramMap.put("userName", userName);
		List<SpecialImage> siList = specialImageMapper.selectSpecialImageListByParm(paramMap);
		if(CollectionUtils.isNotEmpty(siList)){
			List<Long> idsList = new ArrayList<>();
			siList.forEach(ssv -> {
				Long specialImageId = ssv.getSpecialImageId();
				//查询专题id下所有的标注数据
				SpecialAnnotation sa = new SpecialAnnotation();
					sa.setSpecialImageId(specialImageId);
				List<SpecialAnnotation>  list = specialAnnotationMapper.selectAnnotationList(sa);
				if(org.apache.commons.collections.CollectionUtils.isNotEmpty(list)){
					for(SpecialAnnotation an : list){
						//删除标注数据
						specialAnnotationMapper.deleteByPrimaryKey(an.getSliceAnnotationId());
					}
				}
				idsList.add(specialImageId);
			});

			if(CollectionUtils.isNotEmpty(idsList)){
				SpecialImage record = new SpecialImage();
				record.setEditBy(-1l);
				record.setSliceImageStatus(0);
				Long[] idArray = idsList.toArray(new Long[idsList.size()]);
				record.setSpecialImageIds(idArray);
				specialImageMapper.updateByPrimaryKeySelective(record);
				log.info("用户："+userName+" 专题数据是放id:"+idsList.toString());
			}
		}
		
	}
	

	@Override
	public List<SubImage> selectSubImageList(SubImage subImage) {
		Map paramMap = new HashMap<>();
		paramMap.put("special_id", subImage.getSpecialId());
		paramMap.put("parent_image_id", subImage.getParentImageId());
		paramMap.put("slice_batch_number", subImage.getSliceBatchNumber());
		List<SubImage>  list = subImageMapper.selectByMap(paramMap);
		return list;
	}
	
	@Override
	public Map<String,String> getDictInfo(String dictType) {
		Map<String,String> organizationMap  =  new HashMap<String, String>();
		Map<String,Object> map = new HashMap<>();
//			map.put("dictType", SysDictTypeEnum.sysvisceraorganization.label());
			map.put("dictType", dictType);
			map.put("status", 0);
		List<SysDictData> dictDatas =  sysDictDataService.getSysDictDataListByParm(map);
		if(CollectionUtils.isNotEmpty(dictDatas)){
			for(SysDictData sysDate : dictDatas){
				String dictLabel = sysDate.getDictLabel();
				String dictValue = sysDate.getDictValue();
				organizationMap.put(dictValue, dictLabel);
			}
		}
		return organizationMap;
	}

	@Override
	public List<SubImage> selectImageCount(Long organizationId){
		return subImageMapper.selectImageCount(organizationId);
	}

	@Override
	public SysOrganizationAuthorization selectOrganization(Long userId){
		return subImageMapper.selectOrganization(userId);
	}


}
