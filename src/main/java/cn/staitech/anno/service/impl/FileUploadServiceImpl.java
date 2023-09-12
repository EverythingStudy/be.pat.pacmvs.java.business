package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.files.Files;
import cn.staitech.anno.service.FileUploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * @author: wangfeng
 * @create: 2023-09-12 18:12:03
 * @Description:
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {

    String basePath = "";

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
     * @param file
     * @return
     * @throws IOException
     */
    public Files uploadAndProcessBusiness(MultipartFile file, Integer businessType) throws IOException {

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

}
