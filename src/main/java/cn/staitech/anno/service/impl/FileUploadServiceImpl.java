package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.Topic;
import cn.staitech.anno.domain.files.Files;
import cn.staitech.anno.domain.files.in.FileUploadVO;
import cn.staitech.anno.service.FileUploadService;
import cn.staitech.anno.service.FilesService;
import cn.staitech.anno.service.TopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
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
    String basePath = "d://testdir/";

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
    public Files uploadAndProcessBusiness(FileUploadVO fileUploadVO) throws IOException {
        String dirPath = basePath;

        Integer businessType = fileUploadVO.getBusinessType();
        Long topicId = fileUploadVO.getTopicId();
        // 专题列表
        Topic topic = topicService.getById(topicId);

        // 专题名称
        dirPath = basePath + topic.getTopicName();

        //创建文件夹
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = dirPath + "\\" + fileUploadVO.getFileName();

        log.info("------------------filePath:{}", filePath);
        // (真实存入)拷贝
        fileUploadVO.getMultipartFile().transferTo(Paths.get(filePath));

        File file = new File(filePath);

        Files files = new Files();
        BeanUtils.copyProperties(fileUploadVO, files);
        files.setFilesName(fileUploadVO.getFileName());
        files.setFilesPath(file.getAbsolutePath());
        files.setFilesPath(file.getAbsolutePath());
        files.setSize(files.getSize());
        // files.setFormat(file.get);
        filesService.save(files);
        return files;
    }

}
