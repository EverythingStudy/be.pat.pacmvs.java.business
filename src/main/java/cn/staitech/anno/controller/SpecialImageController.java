package cn.staitech.anno.controller;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import cn.staitech.anno.domain.specilaImage.SpecialImage;
import cn.staitech.common.security.annotation.RequiresSpecialPermissions;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SpecialRole;
import cn.staitech.system.api.model.LoginUser;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;

import cn.hutool.json.JSONUtil;
import cn.staitech.anno.constant.SpecialImageConstant;
import cn.staitech.anno.domain.Image;
import cn.staitech.anno.domain.special.Special;
import cn.staitech.anno.domain.vo.special.SpecialResVo;
import cn.staitech.anno.domain.vo.specialImage.InsertSpecialImageVO;
import cn.staitech.anno.domain.vo.specialImage.SpecialImageSelectVO;
import cn.staitech.anno.domain.vo.specialImage.SpecialImageVO;
import cn.staitech.anno.domain.vo.specialImage.WaitSpecialImageVO;
import cn.staitech.anno.service.SpecialImageService;
import cn.staitech.anno.service.SpecialService;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.utils.SpecialPageMaster;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.utils.PageUtils;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: SpecialImageController
 * @Description:专题选片表 前端控制器
 * @date 2023年6月2日
 */
@RestController
@RequestMapping("/specialImage")
@Api(value = "专题选片", tags = "专题选片")
@Slf4j
public class SpecialImageController {


    @Resource
    private SpecialImageService specialImageService;

    @Resource
    private SpecialService specialService;

    @ApiOperation(value = "查询专题下的切片", hidden = true)
    @GetMapping("/selectSpecialId")
    public List<SpecialImage> selectSpecialId(@RequestParam("specialId") Long specialId) {
        return specialImageService.selectSpecialId(specialId);
    }

    /**
     * 选择专题
     *
     * @param req
     * @return
     */
    @ApiOperationSupport(author = "wanglibei")
    @ApiOperation(value = "添加切片-查询全部切片")
    @RequiresSpecialPermissions("specialConfig:pick:topic")
    @Log(title = "专题待选片", menu = "专题管理", subMenu = "专题选片", businessType = BusinessType.OTHER)
    @PostMapping("/selectImage")
    public R<PageMaster<WaitSpecialImageVO>> selectImage(@RequestBody SpecialImageSelectVO req) {
    	if(req.getTopicId() == 0){
    		 return R.fail(SpecialImageConstant.SELECT_IMAGE_ERROR);
    	}
        PageUtils.startPage(req.getPageNum(), req.getPageSize());
        req.setDeleteFlag(1);
        req.setStatus(1);
        List<WaitSpecialImageVO> wsivList = specialImageService.selectWaitSpecialImage(req);
        PageMaster<WaitSpecialImageVO> pageMaster = new PageMaster<>(wsivList);
        String jsonStr = JSONUtil.toJsonStr(R.ok(pageMaster));
        log.info("添加切片-查询全部切片:" + jsonStr);
        return R.ok(pageMaster);
    }

    @ApiOperation(value = "批量添加专题切片")
    @ApiOperationSupport(author = "wanglibei")
    @Log(title = "批量保存切片", menu = "专题管理", subMenu = "保存切片", businessType = BusinessType.INSERT)
    @PostMapping("/batchInsert")
    public R<String> batchInsert(@RequestBody InsertSpecialImageVO vo) throws Exception {
        R<String> r = specialImageService.insertSpecialImageList(vo);
        //		String jsonStr = JSONUtil.toJsonStr(R.ok(R.ok(null,MessageSource.M("OPERATE_ERROR"))));
        //		log.info("批量添加专题切片:"+jsonStr);
        return r;
    }

    /**
     * 专题选片列表
     *
     * @param req
     * @return
     */
    @ApiOperationSupport(author = "wanglibei")
    @ApiOperation(value = "查询专题的切片列表")
//    @RequiresSpecialPermissions("specialConfig:pick:list")
    @Log(title = "切片列表查询", menu = "专题管理", subMenu = "切片列表查询", businessType = BusinessType.OTHER)
    @PostMapping("/selectSpecialImage")
    public R<SpecialPageMaster<SpecialImageVO>> selectSpecialImage(@RequestBody SpecialImageSelectVO req) {
        PageUtils.startPage(req.getPageNum(), req.getPageSize());
        List<SpecialImageVO> wsivList = specialImageService.selectSpecialImageList(req);
        //查询专题是否已经全部交付
//		Special special = specialImageService.selectSpecialByPrimaryKey(req.getSpecialId());
        SpecialResVo special = specialService.selectSpecialId(req.getSpecialId());
        //查询已选所属专题
        int topicId = -1;
        List<Image> imageList = specialImageService.getImageBySpecialId(req);
        if(CollectionUtils.isNotEmpty(imageList)){
        	Image image = imageList.get(0);
        	topicId = image.getTopicId().intValue();
        }
        special.setTopicId(topicId);
        SpecialPageMaster<SpecialImageVO> pageMaster = new SpecialPageMaster<>(wsivList, special.getDeliveryStatus(), special);
        String jsonStr = JSONUtil.toJsonStr(R.ok(pageMaster));
        log.info("查询专题的切片列表:" + jsonStr);
        return R.ok(pageMaster);
    }

}
