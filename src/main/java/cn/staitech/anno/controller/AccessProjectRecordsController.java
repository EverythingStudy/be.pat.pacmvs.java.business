package cn.staitech.anno.controller;

import cn.staitech.anno.service.AccessProjectRecordsService;
import cn.staitech.anno.vo.accessprojectrecords.AccessProjectRecordsOut;
import cn.staitech.common.core.domain.R;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Api(value = "项目访问", tags = "项目访问")
@RestController
@RequestMapping("/accessRecords")
public class AccessProjectRecordsController {

    @Resource
    private AccessProjectRecordsService accessProjectRecordsService;


    @ApiOperationSupport(author = "zmj")
    @ApiOperation(value = "访问项目列表", notes = "访问项目列表")
    @GetMapping("/list")
    public R<List<AccessProjectRecordsOut>> list(){
        List<AccessProjectRecordsOut> records=accessProjectRecordsService.accessRecords();
        return R.ok(records);
    }

}
