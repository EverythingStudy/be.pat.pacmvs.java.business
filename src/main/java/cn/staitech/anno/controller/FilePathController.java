package cn.staitech.anno.controller;

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

    @Value("${slidePath}")
    private String slidePath;

    @Value("${uploadPath}")
    private String uploadPath;

    @PostMapping("/getFilePath")
    public R getFilePath(@RequestBody GetFilePathIn req) {

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
