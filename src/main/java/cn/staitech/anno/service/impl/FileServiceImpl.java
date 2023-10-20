package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.MarkingExamine;
import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.domain.file.Chunk;
import cn.staitech.anno.domain.geojson.GeoLabel;
import cn.staitech.anno.domain.vo.file.SlideFileName;
import cn.staitech.anno.mapper.MarkingExamineMapper;
import cn.staitech.anno.mapper.PathologicalIndicatorCategoryMapper;
import cn.staitech.anno.mapper.SlideMapper;
import cn.staitech.anno.project.domain.Marking;
import cn.staitech.anno.project.domain.Project;
import cn.staitech.anno.project.mapper.MarkingMapperV1;
import cn.staitech.anno.project.mapper.ProjectMapperV1;
import cn.staitech.anno.service.FileService;
import cn.staitech.anno.utils.FileUtils;
import cn.staitech.anno.utils.MessageSource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FileServiceImpl implements FileService {
    private static final String zipUrl = "/home/pat_saas/Data/zipFile/";
    String fileUrl = "/home/pat_saas/Data";

    @Resource
    private SlideMapper slideMapper;

    @Resource
    private MarkingMapperV1 markingMapperV1;

    @Resource
    private PathologicalIndicatorCategoryMapper pathologicalIndicatorCategoryMapper;

    @Resource
    private ProjectMapperV1 projectMapperV1;

    @Resource
    private MarkingExamineMapper markingExamineMapper;


    /**
     * @param slideId 切片id
     * @param suffix  后缀
     * @return
     * @throws Exception
     */
    @Override
    public String createFiles(Long slideId, String suffix) throws Exception {
        // 查询项目表中信息，判断项目是什么类型
        Slide slide = slideMapper.selectById(slideId);
        SlideFileName slideFileName = null;
        Project project = projectMapperV1.selectById(slide.getProjectId());
        // 评审
        if (Objects.equals(project.getProjectType(), "2")) {
            slideFileName = slideMapper.slideReviewFileName(slideId);
        } else {
            // 非评审
            slideFileName = slideMapper.slideFileName(slideId);
        }
        // 生成二级目录 (以专题名称命名)
        String twoFolderName = fileUrl + File.separator + slideFileName.getTopicName();
        createFolder(twoFolderName);
        // 生成三级目录 (以图片名称命名)
        String threeFolderName = twoFolderName + File.separator + slideFileName.getImageName();
        createFolder(threeFolderName);
        String fileUrl = threeFolderName + File.separator + slideFileName.getImageName();
        if (slideFileName.getSlideType() != null) {
            fileUrl += "_" + slideFileName.getSlideType();
        }
        // 根据切片查询标注表中所使用的标签
        QueryWrapper<Marking> markingQueryWrapper = new QueryWrapper<>();
        markingQueryWrapper.select("category_id").eq("slide_id", slideId).ne("category_id", 0).groupBy("category_id");
        List<Marking> markingList = markingMapperV1.selectList(markingQueryWrapper);
        List<GeoLabel> categoryList = new ArrayList<>();
        if (markingList.size() > 0) {
            for (cn.staitech.anno.project.domain.Marking marking : markingList) {
                GeoLabel geoLabel = pathologicalIndicatorCategoryMapper.selectGeoLabel(marking.getCategoryId());
                categoryList.add(geoLabel);
            }
        }
        if (categoryList.size() > 0) {
            StringBuilder categoryNumber = new StringBuilder();
            for (GeoLabel geoLabel : categoryList) {
                categoryNumber.append(geoLabel.getLabel_code());
            }
            fileUrl += "_" + categoryNumber;
        }
        fileUrl += "_" + System.currentTimeMillis() + suffix;
        createFile(fileUrl);
        return fileUrl;
    }


    public String createExamineScoreFiles(Long slideId, String suffix, Long questionProjectId, Long createBy) throws Exception {
        // 查询项目表中信息，判断项目是什么类型
        cn.staitech.anno.domain.Slide slide = slideMapper.selectById(slideId);
        SlideFileName slideFileName = null;
        Project project = projectMapperV1.selectById(slide.getProjectId());
        // 评审
        if (Objects.equals(project.getProjectType(), "2")) {
            slideFileName = slideMapper.slideReviewFileName(slideId);
        } else {
            // 非评审
            slideFileName = slideMapper.slideFileName(slideId);
        }
        // 生成二级目录 (以专题名称命名)
        String twoFolderName = fileUrl + File.separator + slideFileName.getTopicName();
        createFolder(twoFolderName);
        // 生成三级目录 (以图片名称命名)
        String threeFolderName = twoFolderName + File.separator + slideFileName.getImageName();
        createFolder(threeFolderName);
        String fileUrl = threeFolderName + File.separator + slideFileName.getImageName();
        if (slideFileName.getSlideType() != null) {
            fileUrl += "_" + slideFileName.getSlideType();
        }
        // 根据切片查询标注表中所使用的标签
        QueryWrapper<MarkingExamine> markingQueryWrapper = new QueryWrapper<>();
        markingQueryWrapper
                .select("category_id")
                .eq("question_project_id", questionProjectId)
                .eq("create_by", createBy)
                .ne("category_id", 0)
                .groupBy("category_id");
        List<MarkingExamine> markingList = markingExamineMapper.selectList(markingQueryWrapper);
        List<GeoLabel> categoryList = new ArrayList<>();
        if (markingList.size() > 0) {
            for (MarkingExamine marking : markingList) {
                GeoLabel geoLabel = pathologicalIndicatorCategoryMapper.selectGeoLabel(marking.getCategoryId());
                categoryList.add(geoLabel);
            }
        }
        if (categoryList.size() > 0) {
            StringBuilder categoryNumber = new StringBuilder();
            for (GeoLabel geoLabel : categoryList) {
                categoryNumber.append(geoLabel.getLabel_code());
            }
            fileUrl += "_" + categoryNumber;
        }
        fileUrl += "_" + System.currentTimeMillis() + suffix;
        createFile(fileUrl);
        return fileUrl;
    }


    private static Boolean createFile(String url) throws Exception {
        File file = new File(url);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new Exception(MessageSource.M("FILE_DOWNLOAD_ERROR"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    private static Boolean createFolder(String folder) throws Exception {
        File file = new File(folder);
        if (!file.exists() && !file.isDirectory()) {
            if (file.mkdir()) {
                return true;
            } else {
                throw new Exception(MessageSource.M("FILE_DOWNLOAD_ERROR"));
            }
        }
        return true;
    }


    @Override
    public String mergeChunk(Chunk chunk) throws Exception {
        // 切片名称
        // 压缩包文件地址
//        String zipUrl = "/home/uploadPath/zipFile/";


        //切片文件夹
        // 创建空文件夹
        File zipFile = new File(zipUrl);
        if (!zipFile.exists()) {
            FileUtils.createFolder(zipUrl);
        }

        String zipFIleUrl = zipUrl + chunk.getFileName();
        File file = new File(zipFIleUrl);
        if (!file.exists()) {
            FileUtils.createNewzip(zipFIleUrl);
        }
        try (InputStream fis = chunk.getFile().getInputStream(); RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            int len = -1;
            // byte[] buffer = new byte[1024*4];
            byte[] buffer = new byte[1024 * 4 * 10];
            // 指针移动到当前块开始写的位置，chunk.getChunkNumber()是指当前是第几块，减一后乘
            // 以每个块的大小 得到前面块的偏移量，即当前块的起始位置
            raf.seek((chunk.getChunkNumber()) * chunk.getChunkSize());
            //log.info(" ---------------------------> seek:{}", (chunk.getChunkNumber()) * chunk.getChunkSize());
            //把当前块的内容写入
            // java.util.ConcurrentModificationException: null,并发修改异常
            while ((len = fis.read(buffer)) != -1) {
                raf.write(buffer, 0, len);
            }
        } catch (IOException e) {
        }
        return zipFIleUrl;
    }


    public String upload(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            return null;
        }
        //获取文件名
        String fileName = file.getOriginalFilename();
        //获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //获取文件大小
        int fileSize = (int) file.getSize();
        //文件路径
        String path = zipUrl + "\\" + fileName;
        //文件存储路径:保存到数据库
//        String filePath = date+"/"+fileName;
        java.io.File dest = new java.io.File(path);
        // 判断路径是否存在，如果不存在则创建
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            //保存文件
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(path));
            outputStream.write(file.getBytes());
            outputStream.flush();
            outputStream.close();
            return path;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    private static void merge(String dest, File files) {
        // TODO 自动生成的方法存根
        String filename = files.getName();
        System.out.println(files.getName());
        filename = files.getName().substring(0, filename.lastIndexOf("-"));
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest + File.separator + filename));
            BufferedInputStream bis = null;
            byte bytes[] = new byte[1024 * 1024];
            int len = -1;

            bis = new BufferedInputStream(new FileInputStream(files));
            while ((len = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
            }
        } catch (FileNotFoundException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
    }


    private static Boolean merge1(Chunk chunk, File files, String dest) {
//         TODO 自动生成的方法存根
        String filename = files.getName();
        filename = files.getName().substring(0, filename.lastIndexOf("-"));
        try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(files.toPath())); RandomAccessFile raf = new RandomAccessFile(new FileOutputStream(dest + File.separator + filename).toString(), "rw")) {
            int len = -1;
            // byte[] buffer = new byte[1024*4];
            byte[] buffer = new byte[1024 * 4 * 10];
            // 指针移动到当前块开始写的位置，chunk.getChunkNumber()是指当前是第几块，减一后乘
            // 以每个块的大小 得到前面块的偏移量，即当前块的起始位置
            raf.seek((chunk.getChunkNumber()) * chunk.getChunkSize());

            //log.info(" ---------------------------> seek:{}", (chunk.getChunkNumber()) * chunk.getChunkSize());
            //把当前块的内容写入
            // java.util.ConcurrentModificationException: null,并发修改异常
            while ((len = bis.read(buffer)) != -1) {
                raf.write(buffer, 0, len);
            }
        } catch (IOException e) {

            return false;
        }
        return true;
    }


    //    private static void merge(String dest, File files) {
//        // TODO 自动生成的方法存根
//        String filename = files.getName();
//        filename = files.getName().substring(0, filename.lastIndexOf("-"));
//        try {
//            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest + File.separator + filename));
//            BufferedInputStream bis = null;
//            byte bytes[] = new byte[1024 * 1024];
//            int len = -1;
//            bis = new BufferedInputStream(new FileInputStream(files));
//            while ((len = bis.read(bytes)) != -1) {
//                bos.write(bytes, 0, len);
//            }
//        } catch (FileNotFoundException e) {
//            // TODO 自动生成的 catch 块
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO 自动生成的 catch 块
//            e.printStackTrace();
//        }
//    }

}
