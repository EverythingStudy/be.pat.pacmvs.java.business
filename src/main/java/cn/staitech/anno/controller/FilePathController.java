package cn.staitech.anno.controller;

import cn.staitech.anno.project.domain.Project;
import cn.staitech.anno.project.mapper.ProjectMapperV1;
import cn.staitech.anno.utils.StatisticListUtils;
import cn.staitech.anno.vo.filepath.in.GetFilePathIn;
import cn.staitech.common.core.domain.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author wudi
 * @Date 2023/11/10 9:41
 * @desc 获取机构文件路径
 */
@Api(value = "获取文件路径", tags = "获取文件路径")
@RestController
@RequestMapping("/filePath")
@Slf4j
public class FilePathController {

    @Resource
    private ProjectMapperV1 projectMapperV1;
    @GetMapping("/getFilePath")
    public R getFilePath(@RequestBody  GetFilePathIn req){
        if(req.getFlag()==1){
            String fourNumber = StatisticListUtils.getFourNumber(req.getOrganizationId());
            String replace = req.getOldPath().replace("/home/pat_saas", "/home/pat_saas/" + fourNumber);
            return R.ok(replace);
        }else{
            Project project = projectMapperV1.selectById(req.getProjectId());
            String fourNumber = StatisticListUtils.getFourNumber(project.getOrganizationId());
            String replace = req.getOldPath().replace("/home/pat_saas", "/home/pat_saas/" + fourNumber);
            return R.ok(replace);
        }

    }

}
