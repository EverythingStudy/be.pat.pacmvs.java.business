package cn.staitech.anno.service;

import cn.staitech.anno.vo.files.in.FileUploadVO;

public interface JsonFilesService  {

    void uploadAndProcessBusiness(FileUploadVO vo) throws Exception;

    String mergeChunk(FileUploadVO fileUploadVO) throws Exception;
}
