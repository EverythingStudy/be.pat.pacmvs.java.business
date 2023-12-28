package cn.staitech.anno.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.staitech.anno.config.MapConstant;
import cn.staitech.anno.domain.Indicator;
import cn.staitech.anno.domain.Organ;
import cn.staitech.anno.domain.Structure;
import cn.staitech.anno.mapper.OrganMapper;
import cn.staitech.anno.service.IndicatorService;
import cn.staitech.anno.service.OrganService;
import cn.staitech.anno.service.StructureService;
import cn.staitech.anno.utils.LanguageUtils;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.vo.organ.InsertOrganVO;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.utils.bean.BeanUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author: wangfeng
 * @create: 2023-09-10 13:10:18
 * @Description: 种属
 */
@Service
class OrganServiceImpl extends ServiceImpl<OrganMapper, Organ> implements OrganService {

    @Resource
    OrganMapper organMapper;

    @Resource
    private StructureService structureService;

    @Resource
    private IndicatorService indicatorService;


    @Override
    public Map<String, String> selectMap() {
        return select(false);
    }

    @Override
    public Map<String, String> selectMapEn() {
        return select(true);
    }

    /**
     * 根据种属编号获取脏器列表
     *
     * @param speciesId
     * @return
     */
    @Override
    public List<Organ> getOrganBySpeciesId(String speciesCode) {
		Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
		Organ organQ = new Organ();
		organQ.setOrganizationId(organizationId);
		organQ.setSpeciesCode(speciesCode);
        List<Organ> list = organMapper.getOrganBySpeciesId(organQ);
        for (Organ organ : list) {
            // 中英文
            if (LanguageUtils.isEn()) {
                organ.setName(organ.getNameEn());
            }
        }
        return list;
    }

    public Map<String, String> select(boolean en) {
        List<Organ> list = organMapper.selectList();
        if (en) {
			// 20231222wangfeng
            // return list.stream().collect(Collectors.toMap(item -> item.getSpeciesCode().concat(item.getOrganId()), Organ::getNameEn));
            return list.stream().collect(Collectors.toMap(item -> item.getOrganizationId().toString() + item.getSpeciesCode() + item.getOrganId(), Organ::getNameEn));
        } else {
            // return list.stream().collect(Collectors.toMap(item -> item.getSpeciesCode().concat(item.getOrganId()), Organ::getName));
            return list.stream().collect(Collectors.toMap(item -> item.getOrganizationId().toString() + item.getSpeciesCode() + item.getOrganId(), Organ::getName));
        }
    }

    @Override
    public R<Organ> add(InsertOrganVO req) {
        //校验脏器名称是否已经重复
        QueryWrapper<Organ> queryNameWrapper = new QueryWrapper<>();
        queryNameWrapper.eq("name", req.getName());
        queryNameWrapper.eq("organization_id", SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        List<Organ> nameList = organMapper.selectList(queryNameWrapper);
        if (CollectionUtils.isNotEmpty(nameList)) {
            return R.fail(MessageSource.M("InsertOrganVO.NAME.EXIST"));
        }

        //校验脏器编码 是否已经重复
        QueryWrapper<Organ> queryOrganIdWrapper = new QueryWrapper<>();
        queryOrganIdWrapper.eq("organ_id", req.getOrganId());
        queryOrganIdWrapper.eq("organization_id", SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        List<Organ> organWrapperList = organMapper.selectList(queryOrganIdWrapper);
        if (CollectionUtils.isNotEmpty(organWrapperList)) {
            return R.fail(MessageSource.M("InsertOrganVO.ORGANID.EXIST"));
        }

        Organ organ = new Organ();
        if (StringUtils.isEmpty(req.getNameEn())) {
            organ.setNameEn(req.getName());
        }
        BeanUtils.copyProperties(req, organ);
        organ.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        int insertStatus = organMapper.insert(organ);
        if (insertStatus > 0) {
            //1、tb_structure添加A-I 结构标签+ROA+ROE
            // addStructure(organ);
            //2、tb_pathological_indicator增加一条记录
            addIndicator(organ);
            //3、刷新初始话的数据
            MapConstant.ORGAN_MAP = selectMap();
            MapConstant.ORGAN_MAP_EN = selectMapEn();
            MapConstant.STRUCTURE_MAP = structureService.selectMap();
            MapConstant.STRUCTURE_MAP_EN = structureService.selectMapEn();
            return R.ok(organ, MessageSource.M("INSERT_SUCCESS"));
        }
        return R.fail(MessageSource.M("INSERT_FAILURE"));
    }

    private void addIndicator(Organ organ) {
        Indicator indicator = new Indicator();
        indicator.setIndicatorName(organ.getName());
        indicator.setIndicatorNameEn(organ.getName());
        indicator.setSpeciesId(organ.getSpeciesCode());
        indicator.setOrganId(organ.getOrganId());
        indicator.setNumber(organ.getSpeciesCode().concat(organ.getOrganId()));
        indicator.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        indicator.setDelFlag(0);
        indicator.setCreateBy(SecurityUtils.getUserId());
        indicator.setCreateTime(DateUtil.date());
        indicatorService.insertIndicator(indicator);
    }

    private void addStructure(Organ organ) {
        List<Structure> list = new ArrayList<Structure>();

		/*List<String> structureCodeList = new ArrayList<String>();

		structureCodeList.add("A");
		structureCodeList.add("B");
		structureCodeList.add("C");
		structureCodeList.add("D");
		structureCodeList.add("E");
		structureCodeList.add("F");
		structureCodeList.add("G");
		structureCodeList.add("H");
		structureCodeList.add("I");

		//原始Structure编码规则： 种属：1+脏器编码+(F01-F09)
		String structureId = organ.getSpeciesCode()+organ.getOrganId();
		for(int j=1;j<10;j++){
			String structureCode = "F0"+j;
			for(int i=0;i<3;i++){
				Structure structure = new Structure();
				structure.setSpeciesId(organ.getSpeciesCode());
				structure.setOrganId(organ.getOrganId());
				structure.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());

				structureId = structureId+structureCode;
				String perStructureCode = structureCodeList.get(j-1);
				String name = "";
				String nameEn = "";
				String type = "";
				if(i==0){
					//结构编码
					name = perStructureCode;
					nameEn = name;
					type = "RO";
				}else if(i==1){
					//结构编码+标注
					structureId = structureId+"ROA";
					name = perStructureCode+"标注区域";
					nameEn = perStructureCode+" ROA";
					type = "ROA";
				}else if(i==2){
					//结构编码+考核
					structureId = structureId+"ROE";
					name = perStructureCode+"考核区域";
					nameEn = perStructureCode+" ROE";
					type = "ROE";
				}
				structure.setStructureId(structureId);
				structure.setName(name);
				structure.setNameEn(nameEn);
				structure.setType(type);
				list.add(structure);
				structureId = organ.getSpeciesCode()+organ.getOrganId();
			}
		}*/
        //保存处理
        //原始Structure编码规则： 种属：1+脏器编码+(F01-F09)
        String structureId = organ.getSpeciesCode() + organ.getOrganId();
        for (int i = 0; i < 3; i++) {
            Structure structure = new Structure();
            structure.setSpeciesId(organ.getSpeciesCode());
            structure.setOrganId(organ.getOrganId());
            structure.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());

            String name = "";
            String nameEn = "";
            String type = "";
            if (i == 0) {
                //结构编码
                name = structureId;
                nameEn = name;
                type = "RO";
            } else if (i == 1) {
                //结构编码+标注
                structureId = structureId + "ROA";
                name = structureId + "标注区域";
                nameEn = structureId + " ROA";
                type = "ROA";
            } else if (i == 2) {
                //结构编码+考核
                structureId = structureId + "ROE";
                name = structureId + "考核区域";
                nameEn = structureId + " ROE";
                type = "ROE";
            }
            structure.setStructureId(structureId);
            structure.setName(name);
            structure.setNameEn(nameEn);
            structure.setType(type);
            list.add(structure);
            structureId = organ.getSpeciesCode() + organ.getOrganId();
        }
        structureService.saveBatch(list);

    }

}
