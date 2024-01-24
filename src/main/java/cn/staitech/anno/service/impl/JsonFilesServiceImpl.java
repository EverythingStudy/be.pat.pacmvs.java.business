package cn.staitech.anno.service.impl;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.staitech.anno.config.AsyncTask;
import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.constant.Container;
import cn.staitech.anno.domain.Files;
import cn.staitech.anno.service.FilesService;
import cn.staitech.anno.service.JsonFilesService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.OrganizationUtils;
import cn.staitech.anno.vo.files.FileUploadVO;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Enumeration;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


@Slf4j
@Service
public class JsonFilesServiceImpl implements JsonFilesService {


    private final String basePath = "/home/pat_saas";
    private final String zipPath = "/Upload/json/zip";
    @Resource
    private AsyncTask asyncTask;
    @Resource
    private FilesService filesService;


    @Override
    public void uploadAndProcessBusiness(FileUploadVO fileUploadVO) throws Exception {
        String dirPath = basePath + File.separator + OrganizationUtils.geNumber(SecurityUtils.getLoginUser().getSysUser().getOrganizationId()) + zipPath;
        // 文件名称
        String filePath = dirPath + File.separator + fileUploadVO.getFileName();
        File file = new File(filePath);
        if (!file.exists()) {
            fileUploadVO.getMultipartFile().transferTo(Paths.get(filePath));
            parseJson(fileUploadVO.getProjectId(), filePath);
        } else {
            if (file.delete()) {
                fileUploadVO.getMultipartFile().transferTo(Paths.get(filePath));
                parseJson(fileUploadVO.getProjectId(), filePath);
            } else {
                log.error("删除文件失败，当前文件正在解析中");
            }
        }
    }


    @Override
    public String mergeChunk(FileUploadVO chunk) throws Exception {
        Boolean res = true;
        QueryWrapper<Files> filesQueryWrapper = new QueryWrapper<>();
        filesQueryWrapper.eq("files_code", chunk.getUuid());
        filesQueryWrapper.orderByDesc("files_id");
        filesQueryWrapper.last("limit 1");
        Files filesBy = filesService.getOne(filesQueryWrapper);
        if (filesBy == null) {
            Long filesId = saveFiles(chunk);
            filesBy = filesService.getById(filesId);
            // 删除已经有文件
            File file = new File(filesBy.getFilesPath());
            if (file.exists()) {
                // 删除文件
                res = file.delete();
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
        if(res){
            mergeFile(chunk, filesBy.getFilesPath());
        }
        return "1";
    }

    public String mergeFile(FileUploadVO chunk, String path) throws Exception {

        // 将文件数量和文件id添加至map中
        File file = new File(path);
        try (InputStream fis = chunk.getMultipartFile().getInputStream(); RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
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
            log.error("合并文件异常");
            return "0";
        }
        // 删除SET中当前元素
        if (Container.FILE_MAP.containsKey(chunk.getUuid()) && StringUtils.isNotEmpty(chunk.getUuid())) {
            Container.FILE_MAP.get(chunk.getUuid()).remove(chunk.getChunk());
        }
        // 删除ConcurrentHashMap中对应ConcurrentHashSet - map为空时,代表文件上传完成,根据业务类型执行不同业务
        if (Container.FILE_MAP.containsKey(chunk.getUuid()) && Container.FILE_MAP.get(chunk.getUuid()).isEmpty()) {
            // map中删除当前文件信息
            Container.FILE_MAP.remove(chunk.getUuid());
            parseJson(chunk.getProjectId(), path);
        }
        return "1";
    }


    public void parseJson(Long projectId, String fileUrl) throws Exception {

        if (!Optional.ofNullable(projectId).isPresent()) {
            throw new Exception(MessageSource.M("DISALLOW_NOT_PROJECT"));
        }
        //增加大小校验
        boolean tag = zipCheck(fileUrl);
        if (!tag) {
            throw new Exception(MessageSource.M("FILE_LIMIT"));
        }
        asyncTask.zipExport(fileUrl, projectId, SecurityUtils.getLoginUser().getSysUser().getOrganizationId(), SecurityUtils.getUserId());
    }

    public boolean zipCheck(String zipUrl) throws Exception {
        boolean tag = true;
        File file1 = new File(zipUrl);
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
                if (fileSizeInMB > CommonConstant.UPLOAD_FILE_LIMIT) {
                    tag = false;
                    break;
                }
            }
            zp.closeEntry();
        }
        return tag;
    }


    public Long saveFiles(FileUploadVO fileUploadVO) throws Exception {
        String path = basePath + File.separator + OrganizationUtils.geNumber(SecurityUtils.getLoginUser().getSysUser().getOrganizationId()) + zipPath + File.separator + fileUploadVO.getFileName();
        String topicName = "";
        Long topicId = 0L;
        String filesName = fileUploadVO.getFileName();
        // 获取文件后缀
        String suffixName = filesName.substring(filesName.lastIndexOf(".") + 1);

        // 创建文件
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    log.error("创建文件异常");
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


}
