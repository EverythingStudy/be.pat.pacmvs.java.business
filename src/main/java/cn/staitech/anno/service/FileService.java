package cn.staitech.anno.service;

import cn.staitech.anno.domain.file.Chunk;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileService {

    String mergeChunk(Chunk chunk) throws Exception;

    String upload(MultipartFile file) throws Exception;

    String createFolder(Long slideId) throws Exception;

    void createFile(String url)throws Exception;

}
