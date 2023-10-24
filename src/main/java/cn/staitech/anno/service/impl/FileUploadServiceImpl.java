package cn.staitech.anno.service.impl;

import cn.staitech.anno.constant.Container;
import cn.staitech.anno.domain.Topic;
import cn.staitech.anno.domain.file.FileNode;
import cn.staitech.anno.domain.files.Files;
import cn.staitech.anno.domain.files.in.FileUploadVO;
import cn.staitech.anno.service.*;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.common.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.*;

import static cn.staitech.anno.constant.CommonConstant.GLIDE_LINE;

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

    @Resource
    private AlgorithmAssessmentService algorithmAssessmentService;
    private String basePath = "/home/pat_saas";
    private String zipPath = "/home/pat_saas/Upload/json/zip";

    private String uploadPath = File.separator + "home" + File.separator + "pat_saas" + File.separator + "Upload";

    /**
     * @param fileUrl  上传文件路径
     * @param filename 文件名称
     * @return
     */
    public static String getFolderName(String fileUrl, String filename) {
        File file = new File(fileUrl);
        List<FileNode> fileNodeList = new ArrayList<>();
        if (file.isDirectory()) {
            File[] fileArray = file.listFiles();
            for (File f : fileArray) {
                String type = f.isDirectory() ? "dir" : "file";
                FileNode node = new FileNode(f.getName(), f.getAbsolutePath(), type, f.length());
                System.out.println();
                fileNodeList.add(node);
            }
        }

        for (FileNode fileNode : fileNodeList) {
            String name = fileNode.getName();
            System.out.println(name);
            if (name.contains("_")) {
                String res = name.substring(0, name.lastIndexOf("_"));
                if (res.equals(filename)) {
                    return name;
                }
            }
        }
        return filename;
    }

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
    @Transactional(rollbackFor = Exception.class)
    public Files uploadAndProcessBusiness(FileUploadVO fileUploadVO) throws Exception {

        Files files = new Files();

        Integer businessType = fileUploadVO.getBusinessType();

        String dirPath = basePath;

        switch (businessType) {
            case 3:
                if (Objects.equals(fileUploadVO.getTopicName(), "")) {
                    throw new Exception(MessageSource.M("ARGUMENT_INVALID_NOT_FIND_TOPIC"));
                }
                Topic topic = topicService.selectOne(fileUploadVO.getTopicName(), 1);
                // 定义文件夹名称
                dirPath = dirPath + File.separator + "Slides" + File.separator + topic.getTopicName();
                //创建文件夹
                File dir = new File(dirPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                files.setTopicName(topic.getTopicName());
                files.setTopicId(topic.getTopicId());
                break;
            case 4:
                dirPath = zipPath;
                break;
            case 5:
                dirPath = zipPath;
                break;

        }
        String fileName = fileUploadVO.getFileName();
        // 文件名称
        String filePath = dirPath + File.separator + fileName;
        // (真实存入)拷贝+
        File file = new File(filePath);
        if (!file.exists()) {
            fileUploadVO.getMultipartFile().transferTo(Paths.get(filePath));
        } else {
            // 删除文件
            file.delete();
            fileUploadVO.getMultipartFile().transferTo(Paths.get(filePath));
        }
        File localFile = new File(filePath);
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
        files.setCreateBy(SecurityUtils.getUserId());
        files.setCreateTime(new Date());
        files.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        files.setHostId(1);
        files.setBusinessType(businessType);
        filesService.save(files);
        switch (businessType) {
            case 3:
                filesProcessService.prodessByBussinessType(files);
                break;
            case 4:
                if (!Optional.ofNullable(fileUploadVO.getProjectId()).isPresent()) {
                    throw new Exception(MessageSource.M("DISALLOW_NOT_PROJECT"));
                }
                markingService.zipExport(files.getFilesPath(), fileUploadVO.getProjectId());
                break;

            case 5:
                if (!Optional.ofNullable(fileUploadVO.getProjectId()).isPresent()) {
                    throw new Exception(MessageSource.M("DISALLOW_NOT_PROJECT"));
                }
                String fileUrl;
                // 获取json文件最终存储路径
                if (fileUploadVO.getFileUrl() != null) {
                    fileUrl = fileUploadVO.getFileUrl();
                } else {
                    // 获取文件路径
                    fileUrl = uploadPath + File.separator + getFileUrl(fileUploadVO);
                    File dir = new File(fileUrl);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                }
                // 解析zip压缩包
                algorithmAssessmentService.zipExport(files.getFilesPath(), fileUploadVO.getProjectId(), fileUrl);
                break;
        }
        return files;
    }

    @Override
    public Boolean mergeChunk(FileUploadVO chunk) throws Exception {
        // 查询文件是否存在
        QueryWrapper<Files> filesQueryWrapper = new QueryWrapper<>();
        filesQueryWrapper.eq("files_code", chunk.getUuid());
        Files filesBy = filesService.getOne(filesQueryWrapper);
        // 文件为空,第一片文件上传时添加到文件表中
        if (filesBy == null) {
            Long filesId = saveFiles(chunk);
            filesBy = filesService.getById(filesId);
        }
        // 将文件数量和文件id添加至map中
        if (!Container.FILE_MAP.containsKey(chunk.getUuid())) {
            ArrayList<Integer> chunkList = new ArrayList<Integer>();
            for (int i = 0; i < chunk.getChunkTotal(); i++) {
                chunkList.add(i);
            }
            Container.FILE_MAP.put(chunk.getUuid(), chunkList);
        }
        File file = new File(filesBy.getFilesPath());
        if (file.exists()) {
            // 删除文件
            file.delete();
        }
        // 写入文件
        try (InputStream fis = chunk.getMultipartFile().getInputStream();
             RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            int len = -1;
            byte[] buffer = new byte[1024 * 4 * 10];
            // 指针移动到当前块开始写的位置，chunk.getChunkNumber()是指当前是第几块，减一后乘
            // 以每个块的大小 得到前面块的偏移量，即当前块的起始位置
            raf.seek((chunk.getChunk()) * chunk.getChunkSize());
            // 写入文件
            while ((len = fis.read(buffer)) != -1) {
                raf.write(buffer, 0, len);
            }
        } catch (IOException e) {
            return false;
        }
        // 删除map中当前元素
        Container.FILE_MAP.get(chunk.getUuid()).remove(chunk.getChunk());

        // map为空时,代表文件上传完成,根据业务类型执行不同业务
        if (Container.FILE_MAP.get(chunk.getUuid()) != null && Container.FILE_MAP.get(chunk.getUuid()).isEmpty()) {

            // map中删除当前文件信息
            Container.FILE_MAP.remove(chunk.getUuid());

            // 更新文件表中传输状态
            filesBy.setProcessFlag(2);
            filesService.updateById(filesBy);

            // 根据不同业务类型执行
            switch (chunk.getBusinessType()) {
                case 4:
                    if (!Optional.ofNullable(chunk.getProjectId()).isPresent()) {
                        throw new Exception(MessageSource.M("DISALLOW_NOT_PROJECT"));
                    }
                    markingService.zipExport(filesBy.getFilesPath(), chunk.getProjectId());
                    break;
                case 5:
                    if (!Optional.ofNullable(chunk.getProjectId()).isPresent()) {
                        throw new Exception(MessageSource.M("DISALLOW_NOT_PROJECT"));
                    }
                    String fileUrl;
                    // 获取json文件最终存储路径
                    if (chunk.getFileUrl() != null) {
                        fileUrl = uploadPath + File.separator + chunk.getFileUrl();
                    } else {
                        // 获取文件路径
                        fileUrl = uploadPath + File.separator + getFileUrl(chunk);
                    }
                    algorithmAssessmentService.zipExport(filesBy.getFilesPath(), chunk.getProjectId(), fileUrl);
                    break;
            }
        }
        return true;
    }

    public Long saveFiles(FileUploadVO fileUploadVO) {
        String path = null;
        // 根据不同的业务id生成不同的文件
        switch (fileUploadVO.getBusinessType()) {
            case 4:
                // 若有二级目录,生成在获取文件名称上方即可
                path = zipPath + File.separator + fileUploadVO.getFileName();
                break;
            case 5:
                // 若有二级目录,生成在获取文件名称上方即可
                path = zipPath + File.separator + fileUploadVO.getFileName();
                break;
        }
        // 创建文件
        if (path != null) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    log.error("创建文件异常");
                }
            }
        }
        // 获取文件后缀
        String suffixName = fileUploadVO.getFileName().substring(fileUploadVO.getFileName().lastIndexOf("."));

        Files files = Files.builder()
                .filesName(fileUploadVO.getFileName())
                .filesCode(fileUploadVO.getUuid())
                .filesUrl(path)
                .filesPath(path)
                .format(suffixName)
                .processFlag(1)
                .deleteFlag(1)
                .hostId(1)
                .businessType(fileUploadVO.getBusinessType())
                .createTime(new Date())
                .organizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId())
                .createBy(SecurityUtils.getUserId())
                .build();
        // 写入文件表中
        filesService.save(files);
        return files.getFilesId();
    }

    public String getFileUrl(FileUploadVO fileUploadVO) throws Exception {
        if (!Optional.ofNullable(fileUploadVO.getTopicName()).isPresent()) {
            throw new Exception(MessageSource.M("ARGUMENT_INVALID"));
        }
        if (fileUploadVO.getTopicName().length() > 50) {
            throw new Exception(MessageSource.M("ARGUMENT_INVALID"));
        }
        if (!Optional.ofNullable(fileUploadVO.getProjectTypeId()).isPresent()) {
            throw new Exception(MessageSource.M("ARGUMENT_INVALID"));
        }
        if (!Optional.ofNullable(fileUploadVO.getRoundId()).isPresent()) {
            throw new Exception(MessageSource.M("ARGUMENT_INVALID"));
        }
        if (!Optional.ofNullable(fileUploadVO.getNumber()).isPresent()) {
            throw new Exception(MessageSource.M("ARGUMENT_INVALID"));
        }
        String fileName = fileUploadVO.getTopicName() + GLIDE_LINE + fileUploadVO.getProjectTypeId() + fileUploadVO.getRoundId() + GLIDE_LINE + fileUploadVO.getNumber();
        String filesName = getFolderName(uploadPath, fileName);
        String filePath;
        if (Objects.equals(filesName, filesName)) {
            filePath = filesName + GLIDE_LINE + System.currentTimeMillis();
        } else {
            filePath = filesName;
        }
        // 创建二级目录
        return filePath;
    }


}
