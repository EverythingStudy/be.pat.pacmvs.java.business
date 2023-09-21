package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.Topic;
import cn.staitech.anno.domain.files.Files;
import cn.staitech.anno.domain.files.in.FileUploadVO;
import cn.staitech.anno.service.FileUploadService;
import cn.staitech.anno.service.FilesProcessService;
import cn.staitech.anno.service.FilesService;
import cn.staitech.anno.service.TopicService;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

/**
 * @author: wangfeng
 * @create: 2023-09-12 18:12:03
 * @Description:
 */
@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Resource
    private TopicService topicService;

    @Resource
    private FilesService filesService;

    @Resource
    private FilesProcessService filesProcessService;
    private String basePath = "/home/pat_saas";

    /**
     * @param file 上传的文件MultipartFile
     * @return
     * @throws IOException
     */
    public Files upload(MultipartFile file) throws IOException {

        String dirPath = basePath + "/topicName";
        //创建文件夹
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = dirPath + "/" + UUID.randomUUID();
        // (真实存入)拷贝
        file.transferTo(Paths.get(filePath));

        Files files = new Files();

        return files;

    }

    /**
     * @param fileUploadVO
     * @return
     * @throws IOException
     */
    public Files uploadAndProcessBusiness(FileUploadVO fileUploadVO) throws Exception {

        Topic topic = topicService.selectOne(fileUploadVO.getTopicName());

        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();

        Integer businessType = fileUploadVO.getBusinessType();

        String dirPath = basePath;

        switch (businessType) {
            case 3:
                dirPath = dirPath + "\\Data";
                break;
        }


        String fileName = fileUploadVO.getFileName();
        // 专题名称
        dirPath = basePath + topic.getTopicName();

        //创建文件夹
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = dirPath + "\\" + fileName;

        // (真实存入)拷贝
        fileUploadVO.getMultipartFile().transferTo(Paths.get(filePath));

        File localFile = new File(filePath);

        Files files = new Files();
        BeanUtils.copyProperties(fileUploadVO, files);
        files.setFilesName(fileUploadVO.getFileName());
        files.setFilesPath(localFile.getAbsolutePath());
        files.setFilesUrl(localFile.getAbsolutePath());
        files.setSize(localFile.length());
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        files.setFormat(suffixName);
        // 逻辑删除状态（0删除，1未删除）
        files.setDeleteFlag(1);
        // 上传成功
        files.setProcessFlag(1);
        files.setCreateBy(sysUser.getUserId());
        files.setCreateTime(new Date());
        files.setOrganizationId(sysUser.getOrganizationId());
        files.setHostId(1);
        files.setBusinessType(businessType);
        files.setTopicName(topic.getTopicName());
        files.setTopicId(topic.getTopicId());
        filesService.save(files);

        switch (businessType) {
            case 3:
                filesProcessService.prodessByBussinessType(files);
                break;
        }


        return files;
    }

}
