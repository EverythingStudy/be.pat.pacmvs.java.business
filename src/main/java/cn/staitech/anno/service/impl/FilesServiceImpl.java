package cn.staitech.anno.service.impl;

import cn.staitech.anno.config.MapConstant;
import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.constant.Container;
import cn.staitech.anno.domain.Folder;
import cn.staitech.anno.domain.Image;
import cn.staitech.anno.mapper.FilesMapper;
import cn.staitech.anno.service.FilesService;
import cn.staitech.anno.service.FolderService;
import cn.staitech.anno.service.ImageService;
import cn.staitech.anno.service.TopicService;
import cn.staitech.anno.utils.ImgPicCompression;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.OrganizationUtils;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.domain.Files;
import cn.staitech.anno.vo.files.in.FilesListVO;
import cn.staitech.common.core.utils.uuid.IdUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static cn.hutool.crypto.SecureUtil.md5;

/**
 * @author wangf
 * @description 针对表【tb_files】的数据库操作Service实现
 * @createDate 2023-09-10 17:04:40
 */
@Slf4j
@Service
public class FilesServiceImpl extends ServiceImpl<FilesMapper, Files>
        implements FilesService {

    private static final ExecutorService executorService = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2,
            // 空闲线程等待工作的超时时间
            0,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(4096),
            new ThreadFactory() {
                public Thread newThread(Runnable r) {
                    return new Thread(r, "FilesServiceImpl-thread-" + r.hashCode());
                }
            },
            new ThreadPoolExecutor.DiscardOldestPolicy());

    private final String basePath = "/home/pat_saas/";

    @Resource
    private FilesMapper filesMapper;
    @Resource
    private TopicService topicService;
    @Resource
    private FolderService folderService;
    @Resource
    private ImageService imageService;

    /**
     * 查找文件名称中最后一个P、p出现的位置（不包含文件扩展名）
     *
     * @param tmpFileName
     * @return
     */
    private static int getIndex(String tmpFileName) {
        int index = tmpFileName.lastIndexOf("P");

        if (index < 1) {
            index = tmpFileName.lastIndexOf("p");
        }
        return index;
    }

    @Override
    public PageMaster<Files> selectList(FilesListVO req) throws ExecutionException, InterruptedException {
        // 分页
        PageHelper.startPage(req.getPageNum(), req.getPageSize()).setReasonable(true);

        Files files = new Files();
        BeanUtils.copyProperties(req, files);

        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();

        if (req.getTopicName() != null && req.getTopicName() != "" && req.getTopicName() != "null") {
            queryWrapper.like("topic_name", req.getTopicName());
        }
        if (req.getFilesName() != null && !"".equals(req.getFilesName()) && !"null".equals(req.getFilesName())) {
            queryWrapper.like("files_name", req.getFilesName());
        }
        if (req.getCreateTimeParams() != null && req.getCreateTimeParams().containsKey("beginTime")) {
            queryWrapper.ge("create_time", req.getCreateTimeParams().get("beginTime"));
        }
        if (req.getCreateTimeParams() != null && req.getCreateTimeParams().containsKey("endTime")) {
            queryWrapper.le("create_time", req.getCreateTimeParams().get("endTime"));
        }
        if (!SecurityUtils.isAdmin(SecurityUtils.getLoginUser().getSysUser().getUserId())) {
            queryWrapper.eq("organization_id", SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        }
        queryWrapper.ne("format", ".zip").ne("format", "zip");
        queryWrapper.orderByDesc("files_id");

        //  查询图像列表
        List<Files> list = filesMapper.selectList(queryWrapper);

        // 专题列表
        Map<Long, String> topicMap = topicService.selectMap(1);

        for (Files obj : list) {
            // 机构名称
            obj.setOrganizationName(MapConstant.getOrganizationName(obj.getOrganizationId()));

            // 专题名称
            if (topicMap.containsKey(obj.getTopicId())) {
                obj.setTopicName(topicMap.get(obj.getTopicId()));
            }
        }

        PageMaster pageMaster = new PageMaster<>(list);
        pageMaster.setList(list);
        return pageMaster;
    }

    /**
     * 解压压缩包并解析
     *
     * @param files
     * @throws Exception
     */
    @Override
    public void process(Files files) throws Exception {
        String zipFilePath = files.getFilesPath();

        // 1、解析zip压缩包
        if (unZip(zipFilePath)) {
            Long topicId = files.getTopicId();
            String topicName = files.getTopicName();
            Long filesId = files.getFilesId();

            SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
            Long createBy = sysUser.getUserId();
            Long organizationId = sysUser.getOrganizationId();

            // 生成目标文件对应的文件夹路径
            String destDirRootPath = basePath + OrganizationUtils.geNumber(organizationId) + File.separator + "Slides" + File.separator + topicName;

            // 遍历解压后文件夹
            String zipFileRootDir = zipFilePath.substring(0, zipFilePath.lastIndexOf(CommonConstant.FILE_SUFFIX));
            File zipFileSrc = new File(zipFileRootDir);

            // 递归处理文件夹
            if (zipFileSrc.exists() && zipFileSrc.isDirectory()) {
                processDir(topicId, topicName, filesId, createBy, organizationId, destDirRootPath, zipFileSrc);
            }

            // 删除ZIP文件
            File zipFile = new File(zipFilePath);
            if (zipFile.exists() && zipFile.delete()) {
                log.info("压缩文件删除成功:{}", zipFile.getAbsolutePath());
            } else {
                log.info("压缩文件删除失败:{}", zipFile.getAbsolutePath());
            }

            // 删除空文件件
            removeEmptyDir(zipFileSrc);
        } else {
            throw new Exception(MessageSource.M("ZIP_FILE_UNZIP_FAILURE"));
        }

    }

    /**
     * 递归处理文件夹：如果文件夹下没有文件则删除当前文件夹，如果有文件继续递归
     *
     * @param topicId
     * @param topicName
     * @param filesId
     * @param createBy
     * @param organizationId
     * @param destDirRootPath
     * @param directory
     * @throws Exception
     */
    private void processDir(Long topicId, String topicName, Long filesId, Long createBy, Long organizationId, String destDirRootPath, File directory) throws Exception {
        log.info("processDir - 处理文件夹:{}", directory.getAbsolutePath());
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    processDir(topicId, topicName, filesId, createBy, organizationId, destDirRootPath, file);
                    log.info("dir: {}", file.getAbsolutePath());
                } else {
                    processFile(topicId, topicName, filesId, createBy, organizationId, destDirRootPath, file);
                    log.info("file: {}", file.getAbsolutePath());
                }
            }
        }
        // 删除空文件夹
        removeEmptyDir(directory);
    }

    /**
     * 处理图片
     *
     * @param topicId
     * @param topicName
     * @param filesId
     * @param createBy
     * @param organizationId
     * @param destDirRootPath
     * @param file
     * @throws Exception
     */
    private void processFile(Long topicId, String topicName, Long filesId, Long createBy, Long organizationId, String destDirRootPath, File file) throws Exception {
        // INSERT INTO tb_image 源文件名示例：FCPM21-016-CAR20231213D001N1A1234567E01P02
        String fileName = file.getName();

        // 判断文件格式，非jpg,png排除
        String fileExt = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        String tmpFileName = fileName.substring(0, fileName.lastIndexOf('.')).toLowerCase();
        if (!Container.IMAGE_EXT_SET.contains(fileExt)) {
            return;
        }

        // 源文件绝对路径
        String sourcePath = file.getAbsolutePath();

        // 解析目标文件夹名称
        int index = getIndex(tmpFileName);
        if (index < 1) {
            return;
        }

        String folderName = fileName.substring(0, index);
        log.info("sourcePath {} fileName {},length {},index {},folderName {},fileName {}", sourcePath, fileName, fileName.length(), index, folderName, fileName);

        // 目标文件件路径
        File destDir = new File(destDirRootPath, folderName);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        // INSERT INTO aipre_folder
        Folder folder = new Folder();
        folder.setFolderName(folderName);
        folder.setFolderUrl(destDir.getAbsolutePath());
        folder.setFilesId(filesId);
        folder.setOrganizationId(organizationId);
        folder.setCreateBy(createBy);
        // 检查MySQL中是否有该文件夹记录 有读出-无添加
        folder = folderService.selectOne(folder);
        Long folderId = folder.getFolderId();

        // 目标文件路径
        Path destPath = Paths.get(destDirRootPath, folderName, fileName);

        String md5 = md5(file);
        String distPathStr = destPath.toString();

        // check image file exists
        Image image = new Image();
        image.setMd5(md5);
        image.setImagePath(distPathStr);
        image.setOrganizationId(organizationId);

        // 检查是否存在否合条件的记录 - 判断MD5、文件绝对路径是否存在 - 如果相同则直接删除源文件，不移动；如果不同则移动并添加新记录
        if (imageService.exists(image)) {
            log.info("文件存在，删除当前文件 md5:{} organizationId:{} filepath:{}", md5, organizationId, file.getAbsolutePath());
            // 删除当前源文件
            file.delete();
            return;
        }

        // 移动文件 - StandardCopyOption.REPLACE_EXISTING选项表示如果目标文件已经存在，则覆盖原文件。
        java.nio.file.Files.move(Paths.get(sourcePath), destPath, StandardCopyOption.REPLACE_EXISTING);

        image.setFormat(fileExt);
        image.setFileName(fileName.substring(0, fileName.lastIndexOf(CommonConstant.FILE_SUFFIX)));
        image.setImageName(fileName);
        image.setImageUrl(distPathStr);
        image.setFolderId(folderId);
        image.setTopicId(topicId);
        image.setTopicName(topicName);
        image.setCreateBy(createBy);
        image.setCreateTime(new Date());
        // 0上传中、1上传失败、2解析中、3解析失败、4可用
        image.setStatus(4);
        image = imageTransfer(image);

        imageService.save(image);
        log.info("文件处理成功 {} {} => {}", image, sourcePath, distPathStr);

        // 删除空文件夹(目录文件夹)
        removeEmptyDir(destDir);
    }

    /**
     * 解压缩ZIP文件
     *
     * @param zipUrl
     * @return
     * @throws Exception
     */
    public Boolean unZip(String zipUrl) throws Exception {
        // ZIP文件
        File zipFile = new File(zipUrl);

        // zip文件名称，不带扩展名，即新的解压文件夹
        String zipFileNameNoExt = zipUrl.substring(zipUrl.lastIndexOf(File.separator) + 1, zipUrl.lastIndexOf("."));
        // 目标路径根目录
        String destDirRoot = zipUrl.substring(0, zipUrl.lastIndexOf(File.separator) + 1) + zipFileNameNoExt;

        log.info("unZip开始解压文件: {} {}", zipFile.getAbsolutePath(), zipFile.length());

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile), Charset.forName("GBK"))) {
            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {
                // 处理ZIP重复不覆盖逻辑
                String filePath = destDirRoot + "/" + entry.getName();
                File file = new File(filePath);
                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    File parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        int len;
                        byte[] buffer = new byte[1024];
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                entry = zis.getNextEntry();
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("解压异常:{}", zipFile.getAbsolutePath());
            throw new Exception(MessageSource.M("ZIP_FILE_UNZIP_FAILURE"));
        }
        return true;
    }

    /**
     * 文件前置信息上传-初始化文件路径
     * TODO:修改路径
     *
     * @param image
     * @return
     */
    public Image imageTransfer(Image image) throws IOException {
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(image.getImagePath())) {
            // 步骤一：创建 File 对象
            File file = new File(image.getImagePath());
            // 步骤二：读取图片并转换为 BufferedImage 对象
            BufferedImage bufferedImage = ImageIO.read(file);
            // 步骤三：获取图片的宽度
            image.setWidth(String.valueOf(bufferedImage.getWidth()));
            // 步骤四：获取图片的高度
            image.setHeight(String.valueOf(bufferedImage.getHeight()));
            // 获取文件大小
            image.setSize(String.valueOf(file.length()));

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            String folderName = simpleDateFormat.format(new Date());
            String uuid = IdUtils.randomUUID();
            String filePathStr = folderName + "/" + uuid + ".jpg";
            String thumbPath = "/file/statics/" + OrganizationUtils.geNumber(image.getOrganizationId()) + "/thumbnail/" + filePathStr;
            image.setThumbUrl(thumbPath);
            String absFilePath = thumbPath.replace("/file/statics", "/home/pat_saas");

            // 生成缩略图
            ImgPicCompression.doCompress(image.getImagePath(), 256, 256, absFilePath, true);

            image.setImageCode(uuid);
            image.setBizType(7);
        }
        return image;
    }

    /**
     * 删除空文件夹
     *
     * @param file
     */
    private void removeEmptyDir(File file) {
        // 如果根目录为空，删除空文件夹
        if (file.listFiles().length == 0) {
            file.delete();
        }
    }

    /**
     * 异步
     *
     * @param files
     * @return
     */
    @Override
    public void submitTask(Files files) {
        executorService.submit(new ProcessRunnable(files));
    }

    class ProcessRunnable implements Runnable {

        private final Files files;

        ProcessRunnable(Files files) {
            this.files = files;
        }

        @Override
        public void run() {
            try {
                process(files);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
