package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.ImageCsv;
import cn.staitech.anno.domain.files.Files;
import cn.staitech.anno.mapper.ImageCsvMapper;
import cn.staitech.anno.service.CsvParserService;
import cn.staitech.anno.service.FilesProcessService;
import cn.staitech.anno.service.ImageCsvService;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * (ImageCsv)表服务实现类
 *
 * @author makejava
 * @since 2023-09-13 13:20:24
 */
@Slf4j
@Service
public class ImageCsvServiceImpl extends ServiceImpl<ImageCsvMapper, ImageCsv> implements ImageCsvService, FilesProcessService {
    @Resource
    private CsvParserService csvParserService;

    @Override
    public void prodessByBussinessType(Files files) {
        String filePath = files.getFilesPath();
        List<ImageCsv> list = csvParserService.read(filePath, ImageCsv.class);
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        for (ImageCsv imageCsv : list) {
            imageCsv.setTopicId(files.getTopicId());
            imageCsv.setTopicName(files.getTopicName());
            imageCsv.setStatus(1);
            imageCsv.setProcessFlag(1);
            imageCsv.setDeleteFlag(1);
            imageCsv.setHostId(1);

            imageCsv.setCreateBy(sysUser.getUserId());
            imageCsv.setCreateTime(new Date());
            imageCsv.setOrganizationId(sysUser.getOrganizationId());
            this.save(imageCsv);
        }
    }
}

