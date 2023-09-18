package cn.staitech.anno.service;

import cn.staitech.anno.domain.file.Chunk;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileService {

    String mergeChunk(Chunk chunk) throws Exception;

    String upload(MultipartFile file) throws Exception;

    String createFiles(Long slideId,String suffix) throws Exception;


}
