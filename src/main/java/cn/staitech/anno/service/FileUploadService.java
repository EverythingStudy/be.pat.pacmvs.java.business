package cn.staitech.anno.service;

import cn.staitech.anno.domain.files.Files;
import cn.staitech.anno.domain.files.in.FileUploadVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author: wangfeng
 * @create: 2023-09-12 18:11:04
 * @Description: 文件上传
 */

public interface FileUploadService {

    Files upload(MultipartFile file) throws IOException;

    Files uploadAndProcessBusiness(FileUploadVO vo) throws Exception;
}
