package cn.staitech.anno.service.impl;

import cn.staitech.anno.constant.Container;
import cn.staitech.anno.domain.Topic;
import cn.staitech.anno.domain.files.Files;
import cn.staitech.anno.domain.files.in.FileUploadVO;
import cn.staitech.anno.service.*;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.ArrayList;
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
    private MarkingService markingService;

    @Resource
    private FilesProcessService filesProcessService;
    private String basePath = "/home/pat_saas";

//    private String basePath = "D:\\home\\pat_saas";


    private String zipPath = "D:\\home\\upload\\json\\zip";

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
            case 4:
                dirPath = basePath + File.separator + "zip";
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
            case 4:
                markingService.zipExport(files.getFilesPath(), fileUploadVO.getProjectId());
                break;
        }


        return files;
    }

    @Override
    public Boolean mergeChunk(FileUploadVO chunk) throws Exception {
        // 查询文件是否存在
        QueryWrapper<Files> filesQueryWrapper = new QueryWrapper<>();
//        filesQueryWrapper.eq("files_code");


        Files files = filesService.getById(chunk.getFilesId());

        // 将文件数量和文件id添加至map中
        if (!Container.FILE_MAP.containsKey(chunk.getUid())) {
            ArrayList<Integer> chunkList = new ArrayList<Integer>();
            for (int i = 0; i < chunk.getTotalChunks(); i++) {
                chunkList.add(i);
            }
            Container.FILE_MAP.put(files.getFilesId(), chunkList);
        }
        File file = new File(files.getFilesPath());
        // 写入文件
        try (InputStream fis = chunk.getMultipartFile().getInputStream();
             RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            int len = -1;
            byte[] buffer = new byte[1024 * 4 * 10];
            // 指针移动到当前块开始写的位置，chunk.getChunkNumber()是指当前是第几块，减一后乘
            // 以每个块的大小 得到前面块的偏移量，即当前块的起始位置
            raf.seek((chunk.getChunkNumber()) * chunk.getChunkSize());
            // 写入文件
            while ((len = fis.read(buffer)) != -1) {
                raf.write(buffer, 0, len);
            }
        } catch (IOException e) {
            return false;
        }
        // 删除map中当前元素
        Container.FILE_MAP.get(files.getFilesId()).remove(chunk.getChunkNumber());
        // map为空时,代表文件上传完成,根据业务类型执行不同业务
        if (Container.FILE_MAP.get(files.getFilesId()) != null && Container.FILE_MAP.get(files.getFilesId()).isEmpty()) {
            // map中删除当前文件信息
            Container.FILE_MAP.remove(files.getFilesId());
            // 根据不同业务类型执行
            switch (files.getBusinessType()) {
                case 4:
                    markingService.zipExport(files.getFilesPath(), chunk.getProjectId());
                    break;
            }
        }
        return true;
    }


    public Long fileInformation(FileUploadVO fileUploadVO) {
        String dirPath = null;
        // 根据不同业务生成不同文件路径
        switch (fileUploadVO.getBusinessType()) {
            case 4:
                dirPath = zipPath + File.separator + fileUploadVO.getFileName();
                break;
        }
        //创建文件
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File localFile = new File(dirPath);
        Files files = new Files();
        BeanUtils.copyProperties(fileUploadVO, files);
        files.setFilesName(fileUploadVO.getFileName());
        files.setFilesPath(localFile.getAbsolutePath());
        files.setFilesUrl(localFile.getAbsolutePath());
        files.setSize(localFile.length());
        // 获取文件的后缀名
        String suffixName = fileUploadVO.getFileName().substring(fileUploadVO.getFileName().lastIndexOf("."));
        files.setFormat(suffixName);
        // 逻辑删除状态（0删除，1未删除）
        files.setDeleteFlag(1);
        // 上传成功
        files.setProcessFlag(1);
        files.setCreateBy(SecurityUtils.getUserId());
        files.setCreateTime(new Date());
        files.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        files.setHostId(1);
        files.setBusinessType(fileUploadVO.getBusinessType());
        filesService.save(files);
        return files.getFilesId();
    }


}
