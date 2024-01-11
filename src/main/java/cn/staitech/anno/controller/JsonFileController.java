package cn.staitech.anno.controller;

import cn.staitech.anno.service.JsonFilesService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.domain.Files;
import cn.staitech.anno.vo.files.in.FileUploadNoVO;
import cn.staitech.anno.vo.files.in.FileUploadVO;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.annotation.Logical;
import cn.staitech.common.security.annotation.RequiresPermissions;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Objects;

@Api(value = "json文件上传", tags = "json文件上传")
@RestController
@RequestMapping("/jsonFile")
@Slf4j
public class JsonFileController {

    @Resource
    private JsonFilesService jsonfilesService;


    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "文件上传并处理下游业务逻辑", notes = "文件上传并处理下游业务逻辑 - 王峰")
    @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "MultipartFile文件", required = true, dataType = "file"), @ApiImplicitParam(name = "businessType", value = "businessType", required = true, dataType = "Integer")})
    @Log(title = "文件上传并处理下游业务逻辑", menu = "文件上传并处理下游业务逻辑", subMenu = "文件上传并处理下游业务逻辑", businessType = BusinessType.IMPORT)
    @RequiresPermissions(value = {"smartAnnoInfo:algorithm:batchUploadJson", "section:ophthalmology:uploadZip"}, logical = Logical.OR)
    @PostMapping("/uploadBusiness")
    public R<Files> uploadBusiness(@RequestParam("file") MultipartFile file, FileUploadNoVO fileUploadNoVO) throws Exception {
        FileUploadVO fileUploadVO = new FileUploadVO();
        BeanUtils.copyProperties(fileUploadNoVO, fileUploadVO);
        if (checkBusiness(fileUploadVO)) {
            return R.fail(MessageSource.M("ONLY_ZIP_FILE"));
        }
        // 业务校验
        if (checkBusiness(fileUploadVO)) {
            return R.fail(MessageSource.M("ONLY_ZIP_FILE"));
        }
        fileUploadVO.setMultipartFile(file);
        jsonfilesService.uploadAndProcessBusiness(fileUploadVO);
        return R.ok();
    }


    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "文件上传并处理下游业务逻辑(大文件)", notes = "文件上传并处理下游业务逻辑 - 王峰")
    @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "MultipartFile文件", required = true, dataType = "file"), @ApiImplicitParam(name = "businessType", value = "businessType", required = true, dataType = "Integer")})
    @Log(title = "文件上传并处理下游业务逻辑(大文件)", menu = "文件上传并处理下游业务逻辑", subMenu = "文件上传并处理下游业务逻辑", businessType = BusinessType.IMPORT)
    @RequiresPermissions(value = {"smartAnnoInfo:algorithm:batchUploadJson", "section:ophthalmology:uploadZip", "scction:ophthalmology:query"}, logical = Logical.OR)
    @PostMapping("/uploadBigFileBusiness")
    public R<String> uploadBigFileBusiness(@RequestParam("file") MultipartFile file, FileUploadVO fileUploadVO) throws Exception {
        // 业务校验
        if (checkBusiness(fileUploadVO)) {
            return R.fail(MessageSource.M("ONLY_ZIP_FILE"));
        }
        fileUploadVO.setMultipartFile(file);
        String res = jsonfilesService.mergeChunk(fileUploadVO);
        if (Objects.equals(res, "1")) {
            return R.ok(MessageSource.M("FILE_SLIDE_UPLOAD_SUCCESS"));
        } else {
            return R.fail(MessageSource.M("FILE_SLIDE_UPLOAD_FAILURE"));
        }
    }


    public Boolean checkBusiness(FileUploadVO fileUploadVO) {
        String fileName = fileUploadVO.getFileName();
        String fileExt = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        return !"zip".equals(fileExt);
    }


}
