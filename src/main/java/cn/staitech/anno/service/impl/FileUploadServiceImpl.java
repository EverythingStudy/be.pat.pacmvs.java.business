package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.files.Files;
import cn.staitech.anno.domain.files.in.FileUploadVO;
import cn.staitech.anno.service.FileUploadService;
import cn.staitech.anno.service.TopicService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

/**
 * @author: wangfeng
 * @create: 2023-09-12 18:12:03
 * @Description:
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Resource
    private TopicService topicService;
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
        MultipartFile file = fileUploadVO.getFile();
        Long topicId = fileUploadVO.getTopicId();
        // 专题列表
        Map<Long, String> topicMap = topicService.selectMap();

        // 专题名称
        if (topicMap.containsKey(topicId)) {
            dirPath = basePath + topicMap.get(topicId);
        }

        //创建文件夹
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = dirPath + "/" + file.getName();
        // (真实存入)拷贝
        file.transferTo(Paths.get(filePath));

        Files files = new Files();
        return files;
    }

}
