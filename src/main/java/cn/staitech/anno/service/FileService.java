package cn.staitech.anno.service;

import cn.staitech.anno.domain.file.Chunk;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String mergeChunk(Chunk chunk) throws Exception;

    String upload(MultipartFile file) throws Exception;

    String createFiles(Long slideId, String suffix) throws Exception;

    String createExamineScoreFiles(Long slideId, String suffix, Long questionProjectId, Long createBy) throws Exception;


}
