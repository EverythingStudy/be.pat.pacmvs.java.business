package cn.staitech.anno.controller;

import cn.staitech.anno.project.domain.Project;
import cn.staitech.anno.project.mapper.ProjectMapperV1;
import cn.staitech.anno.utils.StatisticListUtils;
import cn.staitech.anno.vo.filepath.GetFilePathIn;
import cn.staitech.common.core.domain.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Value("${slidePath}")
    private String slidePath;

    @Value("${uploadPath}")
    private String uploadPath;

    @PostMapping("/getFilePath")
    public R getFilePath(@RequestBody GetFilePathIn req) {
        //String slidePath = "/home/pat_saas/Slides";
        //String uploadPath = "/home/pat_saas/Upload";
        if (req.getProjectId() != null && req.getProjectId() != 0) {
            Project project = projectMapperV1.selectById(req.getProjectId());
            String fourNumber = StatisticListUtils.getFourNumberNoSlide(project.getOrganizationId());
            String replace = uploadPath.replace("/home/pat_saas", "/home/pat_saas/" + fourNumber);
            return R.ok(replace);
        } else {
            if (req.getFlag() == 1) {
                String fourNumber = StatisticListUtils.getFourNumberNoSlide(req.getOrganizationId());
                String replace = slidePath.replace("/home/pat_saas", "/home/pat_saas/" + fourNumber);
                return R.ok(replace);
            } else {
                String fourNumber = StatisticListUtils.getFourNumberNoSlide(req.getOrganizationId());
                String replace = uploadPath.replace("/home/pat_saas", "/home/pat_saas/" + fourNumber);
                return R.ok(replace);
            }


        }

    }

/*    public static void main(String[] args) {
        String fourNumber = StatisticListUtils.getFourNumberNoSlide(12L);
        String replace = "/home/pat_saas/slides".replace("/home/pat_saas", "/home/pat_saas/" + fourNumber);
        System.out.println(replace);
    }*/
}
