package cn.staitech.anno.service.impl;

import cn.staitech.anno.mapper.FilesMapper;
import cn.staitech.anno.service.FilesService;
import cn.staitech.anno.service.SysOrganizationService;
import cn.staitech.anno.service.TopicService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.files.Files;
import cn.staitech.anno.vo.files.in.FilesListVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * @author wangf
 * @description 针对表【tb_files】的数据库操作Service实现
 * @createDate 2023-09-10 17:04:40
 */
@Slf4j
@Service
public class FilesServiceImpl extends ServiceImpl<FilesMapper, Files>
        implements FilesService {
    @Resource
    private FilesMapper filesMapper;
    @Resource
    private SysOrganizationService organizationService;

    @Resource
    private TopicService topicService;

    @Override
    public PageMaster<Files> selectList(FilesListVO req) throws ExecutionException, InterruptedException {
        // 分页
        PageHelper.startPage(req.getPageNum(), req.getPageSize()).setReasonable(true);

        Files files = new Files();
        BeanUtils.copyProperties(req, files);

        QueryWrapper<Files> queryWrapper = new QueryWrapper<>(files);
        if (req.getTopicName() != null && req.getTopicName() != "" && req.getTopicName() != "null") {
            queryWrapper.like("topic_name", req.getTopicName());
        }
        if (req.getCreateTimeParams() != null && req.getCreateTimeParams().containsKey("beginTime")) {
            queryWrapper.ge("create_time", req.getCreateTimeParams().get("beginTime"));
        }
        if (req.getCreateTimeParams() != null && req.getCreateTimeParams().containsKey("endTime")) {
            queryWrapper.le("create_time", req.getCreateTimeParams().get("endTime"));
        }

        queryWrapper.orderByDesc("files_id");

        //  查询图像列表
        List<Files> list = filesMapper.selectList(queryWrapper);
        // 机构列表
        Map<Long, String> organizationMap = organizationService.selectMap();
        // 专题列表
        Map<Long, String> topicMap = topicService.selectMap(1);

        for (Files obj : list) {
            // 机构名称
            if (organizationMap.containsKey(obj.getOrganizationId())) {
                obj.setOrganizationName(organizationMap.get(obj.getOrganizationId()));
            }
            // 专题名称
            if (topicMap.containsKey(obj.getTopicId())) {
                obj.setTopicName(topicMap.get(obj.getTopicId()));
            }
        }

        PageMaster pageMaster = new PageMaster<>(list);
        pageMaster.setList(list);
        return pageMaster;
    }

    @Override
    public List<String> zipExport(String zipUrl, Long projectId) throws Exception {
        StringBuilder sb;
        File file1 = new File(zipUrl);
        List<String> fileNameList = new ArrayList<>();
        Map<String, String> ddlList = new HashMap<>(16);
        try {
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
            // 循环压缩包中解压内容
            while (zipEnum.hasMoreElements()) {
                // 获取下一个元素
                ze = zipEnum.nextElement();
                sb = new StringBuilder();
                long size = ze.getSize();
                // 获取文件名称
                String fileNames = ze.getName();

/*              文件夹:2042935-OS-4/
                文件：2042935-OS-4/2042935-OS-40-0-0.jpg
                文件：2042935-OS-4/2042935-OS-40-1-0.jpg
                文件：2042935-OS-4/2042935-OS-40-1-1.jpg
                文件：2042935-OS-4/2042935-OS-40-1-2.jpg
                */

                if (ze.isDirectory()) {
                    log.info("文件夹:{}", fileNames);
                }else{
                    log.info("文件：{}", fileNames);

                    if (size > 0) {




                        /*
                        //读取文件内容
                        BufferedReader bf = new BufferedReader(new InputStreamReader(zipFile.getInputStream(ze), StandardCharsets.UTF_8));
                        String line;
                        while ((line = bf.readLine()) != null) {
                            sb.append(line);
                        }
                        // 获取文件内容并转换成string
                        String fileContent = sb.toString();


                        // 获取文件中的内容
                        org.json.JSONObject jsonObject = new org.json.JSONObject(fileContent);
                        // 获取图像相关信息
                        org.json.JSONObject image = jsonObject.getJSONObject("image");
                        // 获取标签相关信息
                        org.json.JSONArray labelInfo = jsonObject.getJSONArray("label_info");
                        // 标签列表长度超出一个，抛出异常
                        if (labelInfo.length() > 1) {
                            fileNameList.add(ze.getName());
                            continue;
                        }
                        if (image != null) {
                            // 获取标注名称
                            String imageName = image.getString("image_name");

                            if (imageName != null) {
                                log.info("{}", fileNames);
                                // 写入数据库
                                // writeAlgorithm(projectId, imageName, fileContent, fileUrl, fileNames);
                            }
                            //这里是对读取的文件内容进行处理
                            ddlList.put(ze.getName(), sb.toString());
                            bf.close();
                        }*/
                    }
                }
                zp.closeEntry();
            }
        } catch (Exception e) {
            throw new Exception(MessageSource.M("JSON_FILE_PARSE_FAILURE"));
        }
        return fileNameList;
    }

}




