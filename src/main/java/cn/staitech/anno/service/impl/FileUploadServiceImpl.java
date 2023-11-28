package cn.staitech.anno.service.impl;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.staitech.anno.config.AsyncTask;
import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.constant.Container;
import cn.staitech.anno.domain.Topic;
import cn.staitech.anno.service.*;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.OrganizationUtils;
import cn.staitech.anno.vo.file.FileNode;
import cn.staitech.anno.vo.files.Files;
import cn.staitech.anno.vo.files.in.FileUploadVO;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

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
    private AsyncTask asyncTask;
    @Resource
    private FilesProcessService filesProcessService;
    @Resource
    private AlgorithmAssessmentService algorithmAssessmentService;
    private String basePath = "/home/pat_saas";
    private String zipPath = "/Upload/json/zip";

    private String uploadPath = File.separator + "Upload";


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
            if (name.contains(GLIDE_LINE)) {
                String res = name.substring(0, name.lastIndexOf(GLIDE_LINE));
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
    @Override
    public Files upload(MultipartFile file) throws IOException {

        String dirPath = basePath + File.separator + OrganizationUtils.geNumber(SecurityUtils.getLoginUser().getSysUser().getOrganizationId()) + "/topicName";
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
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Files uploadAndProcessBusiness(FileUploadVO fileUploadVO) throws Exception {

        Files files = new Files();
        Integer businessType = fileUploadVO.getBusinessType();
        String dirPath = basePath + File.separator + OrganizationUtils.geNumber(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());

        switch (businessType) {
            case 3:
            case 6:
                if (Objects.equals(fileUploadVO.getTopicName(), "")) {
                    throw new Exception(MessageSource.M("ARGUMENT_INVALID_NOT_FIND_TOPIC"));
                }
                Integer projectTypeId = businessType == 6 ? 6 : 1;
                Topic topic = topicService.selectOne(fileUploadVO.getTopicName(), projectTypeId);
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
            case 5:
                dirPath = dirPath + zipPath;
                break;

        }

        String fileName = fileUploadVO.getFileName();
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 文件名称
        String filePath = dirPath + File.separator + fileName;


        // ZIP重复上传重命名逻辑
        if (businessType == 6) {
            if (Objects.equals(fileUploadVO.getTopicName(), "")) {
                throw new Exception(MessageSource.M("ARGUMENT_INVALID_NOT_FIND_TOPIC"));
            }
            String topicName = files.getTopicName();
            Long topicId = files.getTopicId();

            String filesName = fileUploadVO.getFileName();
            // 定义文件夹名称
            // String path = basePath + File.separator + "Slides" + File.separator + topicName + File.separator + filesName;
            String path = dirPath + File.separator + filesName;
            // 重复文件重命名规则
            QueryWrapper<Files> filesQueryWrapper = new QueryWrapper<>();
            filesQueryWrapper.eq("topic_id", topicId);
            filesQueryWrapper.likeRight("files_name", filesName.substring(0, filesName.lastIndexOf(".")));

            List<Files> filesList = filesService.list(filesQueryWrapper);
            if (filesList.size() > 0) {
                String pathPre = path.substring(0, path.lastIndexOf(CommonConstant.FILE_SUFFIX));
                String pathEnd = path.substring(path.lastIndexOf(CommonConstant.FILE_SUFFIX), path.length());
                int index = filesList.size();
                path = pathPre + "(" + index + ")" + pathEnd;
                filesName = filesName.substring(0, filesName.lastIndexOf(CommonConstant.FILE_SUFFIX)) + "(" + index + ")" + suffixName;

                filePath = path;
                fileName = filesName;
            }

        }


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
                //增加大小校验
                /*boolean tag = zipCheck(files.getFilesPath(), fileUploadVO.getProjectId());
                if(!tag){
                	throw new Exception("文件上限300M");
                }*/
                
                asyncTask.zipExport(files.getFilesPath(), fileUploadVO.getProjectId());
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
                    fileUrl = basePath + File.separator + OrganizationUtils.geNumber(SecurityUtils.getLoginUser().getSysUser().getOrganizationId()) + uploadPath + File.separator + getFileUrl(fileUploadVO);
                    File dir = new File(fileUrl);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                }
                // 解析zip压缩包
                List<String> fileNameList = algorithmAssessmentService.zipExport(files.getFilesPath(), fileUploadVO.getProjectId(), fileUrl);
                files.setFileNameList(fileNameList);
                break;
            case 6:
                // 解析文件
                filesService.process(files);
                break;
        }
        return files;
    }

    @Override
    public String mergeChunk(FileUploadVO chunk) throws Exception {
        // 查询文件是否存在
        QueryWrapper<Files> filesQueryWrapper = new QueryWrapper<>();
        filesQueryWrapper.eq("files_code", chunk.getUuid());
        filesQueryWrapper.orderByDesc("files_id");
        filesQueryWrapper.last("limit 1");

        Files filesBy = filesService.getOne(filesQueryWrapper);

        // 文件为空,第一片文件上传时添加到文件表中
        if (filesBy == null) {
            Long filesId = saveFiles(chunk);
            filesBy = filesService.getById(filesId);

            // 删除已经有文件
            File file = new File(filesBy.getFilesPath());
            if (file.exists()) {
                // 删除文件
                file.delete();
            }

            // 将文件数量和文件id添加至map中
            if (!Container.FILE_MAP.containsKey(chunk.getUuid())) {

                ConcurrentHashSet<Integer> chunkSet = new ConcurrentHashSet<Integer>(chunk.getChunkTotal());
                for (int i = 0; i < chunk.getChunkTotal(); i++) {
                    chunkSet.add(i);
                }
                Container.FILE_MAP.put(chunk.getUuid(), chunkSet);
            }
        }

        // 写入文件
        File file = new File(filesBy.getFilesPath());

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
            return "0";
        }
        // 删除map中当前元素
        Container.FILE_MAP.get(chunk.getUuid()).remove(chunk.getChunk());

        // map为空时,代表文件上传完成,根据业务类型执行不同业务
        if (Container.FILE_MAP.get(chunk.getUuid()) != null && Container.FILE_MAP.get(chunk.getUuid()).isEmpty()) {
            // map中删除当前文件信息
            Container.FILE_MAP.remove(chunk.getUuid());

            // 更新文件大小
            filesBy.setSize(file.length());
            // 更新文件表中传输状态
            filesBy.setProcessFlag(2);
            filesService.updateById(filesBy);

            // 根据不同业务类型执行
            switch (chunk.getBusinessType()) {
                case 4:
                    if (!Optional.ofNullable(chunk.getProjectId()).isPresent()) {
                        throw new Exception(MessageSource.M("DISALLOW_NOT_PROJECT"));
                    }
//                    markingService.zipExport(filesBy.getFilesPath(), chunk.getProjectId());
                    /*boolean tag = zipCheck(filesBy.getFilesPath(), chunk.getProjectId());
                    if(!tag){
                    	throw new Exception("文件上限300M");
                    }*/
                    asyncTask.zipExport(filesBy.getFilesPath(), chunk.getProjectId());
                    break;
                case 5:
                    if (!Optional.ofNullable(chunk.getProjectId()).isPresent()) {
                        throw new Exception(MessageSource.M("DISALLOW_NOT_PROJECT"));
                    }
                    String fileUrl;
                    // 获取json文件最终存储路径
                    if (chunk.getFileUrl() != null) {
                        fileUrl = basePath + File.separator + OrganizationUtils.geNumber(SecurityUtils.getLoginUser().getSysUser().getOrganizationId()) + uploadPath + File.separator + chunk.getFileUrl();
                    } else {
                        // 获取文件路径
                        fileUrl = basePath + File.separator + OrganizationUtils.geNumber(SecurityUtils.getLoginUser().getSysUser().getOrganizationId()) + uploadPath + File.separator + getFileUrl(chunk);
                    }
                    List<String> fileNameList = algorithmAssessmentService.zipExport(filesBy.getFilesPath(), chunk.getProjectId(), fileUrl);
                    return fileNameList.toString();
                case 6:
                    // 解析文件
                    filesService.process(filesBy);
                    break;
            }
        }
        return "1";
    }
    
    
    public boolean zipCheck(String zipUrl, Long projectId) throws Exception  {
    	boolean tag = true;
    	File file1 = new File(zipUrl);
    	//        try {
    	//zip可以包含对个文件，如果只有一个文件，则只解析一个文件的，包含多个文件则分别解析
    	//必须指明读取的各式，不然会存在问题
    	ZipFile zipFile = new ZipFile(file1, Charset.forName("gbk"));
    	//按流的方式读取文件，输入到管道中
    	InputStream in = new BufferedInputStream(java.nio.file.Files.newInputStream(file1.toPath()));
    	//字节流转换为压缩文件输入流，通常用来读取压缩文件
    	ZipInputStream zp = new ZipInputStream(in);
    	//定义文件条目
    	ZipEntry ze;
    	Enumeration<? extends ZipEntry> zipEnum = zipFile.entries();
    	// 循环压缩包中解压内容==>TODO 增加文件大小的校验
    	while (zipEnum.hasMoreElements()) {
    		// 获取下一个元素
    		ze = zipEnum.nextElement();
    		if (!ze.isDirectory()) {
    			long size = ze.getSize();
    			//大小计算
    			double fileSizeInMB = (double) size / (1024 * 1024);
    			if (fileSizeInMB >  CommonConstant.UPLOAD_FILE_LIMIT) {
    				tag = false;
    				break;
    			}
    		}
    		zp.closeEntry();
    	}
    	//        } catch (Exception e) {
    	//            throw new Exception("json文件解析失败");
    	//        }
    	//        return true;
    	return tag;
    }

    public Long saveFiles(FileUploadVO fileUploadVO) throws Exception {
        String path = null;
        String topicName = "";
        Long topicId = 0L;
        String filesName = fileUploadVO.getFileName();
        // 获取文件后缀
        String suffixName = filesName.substring(filesName.lastIndexOf(".") + 1);

        // 根据不同的业务id生成不同的文件
        switch (fileUploadVO.getBusinessType()) {
            case 4:
            case 5:
                // 若有二级目录,生成在获取文件名称上方即可
                path = basePath + File.separator + OrganizationUtils.geNumber(SecurityUtils.getLoginUser().getSysUser().getOrganizationId()) + zipPath + File.separator + fileUploadVO.getFileName();
                break;
            case 6:
                if (Objects.equals(fileUploadVO.getTopicName(), "")) {
                    throw new Exception(MessageSource.M("ARGUMENT_INVALID_NOT_FIND_TOPIC"));
                }
                Topic topic = topicService.selectOne(fileUploadVO.getTopicName(), 6);
                topicName = topic.getTopicName();
                topicId = topic.getTopicId();
                // 定义文件夹名称
                path = basePath + File.separator + OrganizationUtils.geNumber(SecurityUtils.getLoginUser().getSysUser().getOrganizationId()) + "/Slides" + File.separator + topicName + File.separator + fileUploadVO.getFileName();
                // 重复文件重命名规则
                QueryWrapper<Files> filesQueryWrapper = new QueryWrapper<>();
                filesQueryWrapper.eq("topic_id", topicId);
                filesQueryWrapper.likeRight("files_name", filesName.substring(0, filesName.lastIndexOf(".")));

                List<Files> filesList = filesService.list(filesQueryWrapper);
                if (filesList.size() > 0) {
                    String pathPre = path.substring(0, path.lastIndexOf(CommonConstant.FILE_SUFFIX));
                    String pathEnd = path.substring(path.lastIndexOf(CommonConstant.FILE_SUFFIX), path.length());
                    int index = filesList.size();
                    path = pathPre + "(" + index + ")" + pathEnd;
                    filesName = filesName.substring(0, filesName.lastIndexOf(CommonConstant.FILE_SUFFIX)) + "(" + index + ")" + suffixName;
                }
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


        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();

        Files files = Files.builder()
                .filesName(filesName)
                .filesCode(fileUploadVO.getUuid())
                .filesUrl(path)
                .filesPath(path)
                .format(suffixName)
                .processFlag(1)
                .deleteFlag(1)
                .hostId(1)
                .businessType(fileUploadVO.getBusinessType())
                .topicId(topicId)
                .topicName(topicName)
                .createTime(new Date())
                .organizationId(sysUser.getOrganizationId())
                .createBy(sysUser.getUserId())
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
        String upath = basePath + File.separator + OrganizationUtils.geNumber(SecurityUtils.getLoginUser().getSysUser().getOrganizationId()) + uploadPath;
        String filesName = getFolderName(upath, fileName);
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
