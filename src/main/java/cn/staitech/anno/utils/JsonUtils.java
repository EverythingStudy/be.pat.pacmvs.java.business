package cn.staitech.anno.utils;

import cn.staitech.anno.domain.ExaminationLog;
import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.vo.annotation.AnnotationJsonVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import static cn.staitech.anno.constant.CommonConstant.FILE_SUFFIX_JSON;

/**
 * JSON生成工具类
 *
 * @author: YL
 * @email: yangl@staitech.cn
 * @date: 2022/9/9 星期五 14:49
 */
@Slf4j
public class JsonUtils {

    /**
     * 判断目录(目录不存则创建、目录存在则清空目录)
     *
     * @param directories
     */
    public static void createDirectories(File directories) {
        if (!directories.exists() && !directories.isDirectory()) {
            directories.mkdirs();
        } else {
            File[] files = directories.listFiles();
            assert files != null;
            for (File file1 : files) {
                file1.delete();
            }
        }
    }

    /**
     * 创建文件
     *
     * @param annotationList
     * @param file
     * @throws IOException
     */
    public static void createFile(List<AnnotationJsonVO> annotationList, File file, Slide slide) throws IOException {
        ExaminationLog examinationLog = new ExaminationLog();
        if (file.createNewFile()) {
            String jsonString = JSON.toJSONString(annotationList, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue);
            log.info("【交付信息】\r\n" + jsonString);

            writeFile(jsonString, file);
            formatFile(file);
            ExaminationUtils.examinationStateLog(examinationLog, slide, 4);
        }
    }

    /**
     * 生成文件
     *
     * @param jsonString
     * @param file
     * @throws IOException
     */
    public static void writeFile(String jsonString, File file) throws IOException {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {
            fos = new FileOutputStream(file, false);
            bos = new BufferedOutputStream(fos);

            fos.write(jsonString.getBytes());
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化文件
     *
     * @param file
     */
    private static void formatFile(File file) {

        // 初始化熟知文本WKT阅读器，可以将WKT文本转换为Geometry对象
        WKTReader wktReader = GeometryUtil.initWktReader();

        try {
            String content = FileUtils.readFileToString(file, "UTF-8");

            //实例化FileOutputStream
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            //将字符流转换为字节流
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            //创建字符缓冲输出流对象
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            JSONArray objects = new JSONArray(JSON.parseArray(content));
            for (int i = 0; i < objects.size(); i++) {
                String location = objects.getJSONObject(i).getString("coordinates");
                Geometry geometry = wktReader.read(location);
                String geometryType = geometry.getGeometryType();
                Coordinate[] coordinates = geometry.getCoordinates();
                List<List<Double>> polygon = new ArrayList<>();

                if ("Point".equals(geometryType)) {
                    Coordinate coordinate = coordinates[0];
                    List<Double> point = new ArrayList<>((Arrays.asList(coordinate.x, coordinate.y)));
                    objects.getJSONObject(i).put("coordinates", point);
                    objects.getJSONObject(i).put("type", geometryType);

                } else if ("LineString".equals(geometryType)) {
                    for (Coordinate coordinate : coordinates) {
                        List<Double> point = new ArrayList<>((Arrays.asList(coordinate.x, coordinate.y)));
                        polygon.add(point);
                    }
                    objects.getJSONObject(i).put("coordinates", polygon);
                    objects.getJSONObject(i).put("type", geometryType);
                } else if ("Polygon".equals(geometryType)) {
                    List<List<List<Double>>> resultLocation = new ArrayList<>();
                    for (Coordinate coordinate : coordinates) {
                        List<Double> point = new ArrayList<>((Arrays.asList(coordinate.x, coordinate.y)));
                        polygon.add(point);
                    }
                    resultLocation.add(polygon);
                    objects.getJSONObject(i).put("coordinates", resultLocation);
                    objects.getJSONObject(i).put("type", geometryType);
                }
            }
            String string = objects.toString(SerializerFeature.PrettyFormat);
            // 将格式化的jsonarray字符串写入文件
            bufferedWriter.write(string);
            // 清空缓冲区，强制输出数据
            bufferedWriter.flush();
            // 关闭输出流
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 保存json 文件
     *
     * @param jsonString 要保存的JSON串
     * @param filePath   保存到的文件路径
     * @param fileName   文件名称
     * @return
     */
    public static boolean createJsonFile(String jsonString, String filePath, String fileName) {
        // 标记文件生成是否成功
        boolean flag = true;
        // 拼接文件完整路径
        String fullPath = filePath + File.separator + fileName + FILE_SUFFIX_JSON;

        // 生成json格式文件
        try {
            // 保证创建一个新文件
            File file = new File(fullPath);
            // 如果父目录不存在，创建父目录
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            // 如果已存在,删除旧文件
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            // 格式化json字符串
            //jsonString = JsonFormatTool.formatJson2(jsonString);

            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            write.write(jsonString);
            write.flush();
            write.close();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }

        // 返回是否成功的标记
        return flag;
    }

    /**
     * 生成随机文件名：当前年月日时分秒+五位随机数
     *
     * @return
     */
    public static String getRandomFileName() {

        SimpleDateFormat simpleDateFormat;

        simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

        Date date = new Date();

        String str = simpleDateFormat.format(date);
        Random random = new Random();
        // 获取5位随机数
        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;
        // 当前时间
        return rannum + str;
    }

    /**
     * JSON文件反序列化 参考：https://blog.csdn.net/weixin_44077403/article/details/127430738
     * String json = JsonUtils.convertStreamToString(file.getInputStream());
     * <p>
     * 读取文件转换json
     *
     * @param inputStream
     * @return
     */
    public static String convertStreamToString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
