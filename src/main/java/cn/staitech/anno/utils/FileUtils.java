package cn.staitech.anno.utils;

import cn.staitech.anno.vo.geojson.Features;
import cn.staitech.anno.vo.marking.MarkGeojson;
import cn.staitech.common.security.utils.SecurityUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

@Slf4j
public class FileUtils {
    private static final String FILE_URL = "/home/pat_saas";
    private static final byte[] ZIP_HEADER_1 = new byte[]{80, 75, 3, 4};
    private static final byte[] ZIP_HEADER_2 = new byte[]{80, 75, 5, 6};

    public static boolean isArchiveFile(File file) {

        if (file == null) {
            return false;
        }
        if (file.isDirectory()) {
            return false;
        }
        boolean isArchive = false;
        InputStream input = null;
        try {
            input = new FileInputStream(file);
            byte[] buffer = new byte[4];
            int length = input.read(buffer, 0, 4);
            if (length == 4) {
                isArchive = (Arrays.equals(ZIP_HEADER_1, buffer)) || (Arrays.equals(ZIP_HEADER_2, buffer));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return isArchive;
    }


    /**
     * Java文件操作 获取文件扩展名(后缀)
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * Java文件操作 获取不带扩展名的文件名
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    public static String createFile(Long imageId) {
        String fileUrl = FILE_URL + File.separator + OrganizationUtils.geNumber(SecurityUtils.getLoginUser().getSysUser().getOrganizationId()) + "/Data/geojson";
        if (!FileUtils.createFolder(fileUrl)) {
            log.error("创建文件夹失败");
        }
        String geojsonUrl = fileUrl + File.separator + imageId + ".geojson";
        File file = new File(geojsonUrl);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    // 写入初始化数据
                    FileUtils.writeFile(geojsonUrl, imageId);
                    return geojsonUrl;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return geojsonUrl;
    }


    public static boolean createFolder(String fileUrl) {
        File folder = new File(fileUrl);
        if (!folder.exists() && !folder.isDirectory()) {
            return folder.mkdirs();
        }
        return false;
    }

    /**
     * 创建空zip
     *
     * @param zipath
     * @throws Exception
     */
    public static void createNewzip(String zipath) throws Exception {
        ZipOutputStream zoutput = null;
        try {
            File file = new File(zipath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fOutputStream = new FileOutputStream(file);
            zoutput = new ZipOutputStream(fOutputStream);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            zoutput.closeEntry();
            zoutput.close();
        }
    }


    /**
     * 输入图片id,将初始化数据输入
     *
     * @param geojsonUrl
     * @param slideId
     * @return
     */
    public static boolean writeFile(String geojsonUrl, Long slideId) {
        // 打开文件,写入固定内容
        MarkGeojson markGeojson = new MarkGeojson();
        List<Features> markings = new ArrayList<>();
        markGeojson.setType("FeatureCollection");
        markGeojson.setFeatures(markings);
        if (!createJsonFile(markGeojson, geojsonUrl)) {
            log.error("文件写入失败");
        }
        return true;
    }


    /**
     * 将JSON数据格式化并保存到文件中
     *
     * @param jsonData 需要输出的json数
     * @param filePath 输出的文件地址
     * @return true || false
     */
    public static boolean createJsonFile(Object jsonData, String filePath) {
        String content = JSON.toJSONString(jsonData, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
        // 标记文件生成是否成功
        boolean flag = true;
        // 生成json格式文件
        try {
            File file = new File(filePath);
            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8);
            write.write(content);
            write.flush();
            write.close();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 把一个文件中的内容读取成一个String字符串
     *
     * @param jsonFile
     * @return
     */
    public static String getStr(File jsonFile) {
        String jsonStr;
        try {
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(Files.newInputStream(jsonFile.toPath()), StandardCharsets.UTF_8);
            int ch;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取一个文件的json数据
     *
     * @param geojsonUrl 文件路径
     * @return JSONObject
     */
    public static JSONObject getAnnotation(String geojsonUrl) {
        JSONObject parse = new JSONObject();
        File jsonFile = new File(geojsonUrl);
        if (jsonFile.exists()) {
            //通过getStr方法获取json文件的内容
            String jsonData = getStr(jsonFile);
            //转json对象
            parse = (JSONObject) JSONObject.parse(jsonData);
            return parse;
        }
        return parse;
    }

    public static Features selectById(Long annotationId, String geojsonUrl) {
        // 读取原始json文件并进行操作和输出
        File jsonFile = new File(geojsonUrl);
        //通过getStr方法获取json文件的内容
        String jsonData = getStr(jsonFile);
        //转json对象
        JSONObject parse = (JSONObject) JSONObject.parse(jsonData);
        //获取主要数据
        JSONArray features = parse.getJSONArray("features");
        // 查找单个元素
        Features marking = new Features();
        List<Object> res = features.stream().filter(s -> annotationId.equals(((JSONObject) s).get("id"))).collect(Collectors.toList());
        for (Object i : res) {
            marking = JSONObject.toJavaObject((JSON) i, Features.class);
        }
        return marking;
    }


    public static boolean addGeojson(Features marking, String geojsonUrl, Long slideId) {
        // 读取原始json文件并进行操作和输出
        File jsonFile = new File(geojsonUrl);
        if (!jsonFile.exists()) {
            FileUtils.createFile(slideId);
        }
        //通过getStr方法获取json文件的内容
        String jsonData = getStr(jsonFile);
        //转json对象
        JSONObject parse = (JSONObject) JSONObject.parse(jsonData);
        //获取主要数据
        JSONArray features = parse.getJSONArray("features");
        features.add(marking);
        return FileUtils.createJsonFile(parse, geojsonUrl);
    }


    public static boolean delGeojson(Long annotationId, String geojsonUrl) {
        // 读取原始json文件并进行操作和输出
        File jsonFile = new File(geojsonUrl);
        if (!jsonFile.exists()) {
            return false;
        }
        //通过getStr方法获取json文件的内容
        String jsonData = getStr(jsonFile);
        //转json对象
        JSONObject parse = (JSONObject) JSONObject.parse(jsonData);
        //获取主要数据
        JSONArray features = parse.getJSONArray("features");
        // 删除元素
        boolean key = features.removeIf(s -> annotationId.equals(((JSONObject) s).get("marking_id")));
        Iterator<Object> o = features.iterator();
        while (o.hasNext()) {
            JSONObject jo = (JSONObject) o.next();
            JSONObject properties = jo.getJSONObject("properties");
            if (Objects.equals(properties.getLongValue("marking_id"), annotationId)) {
                //ja.remove(jo); //不要用这种方式删除，会报出ConcurrentModificationException
                o.remove(); //这种方式OK的
            }
        }
        // 将文件写入json中
        if (FileUtils.createJsonFile(parse, geojsonUrl)) {
            return true;
        }
        return key;
    }


    public static JSONObject exportJson(String geojsonUrl, Long status) {
        // 读取原始json文件并进行操作和输出
        JSONArray featuresList = new JSONArray();
        JSONObject parse = new JSONObject();
        if (geojsonUrl != null) {
            File jsonFile = new File(geojsonUrl);
            if (jsonFile.exists()) {
                //通过getStr方法获取json文件的内容
                String jsonData = getStr(jsonFile);
                //转json对象
                parse = (JSONObject) JSONObject.parse(jsonData);

                if (parse != null) {
                    JSONArray features = parse.getJSONArray("features");
                    // 遍历数组
                    for (Object feature : features) {
                        JSONObject featureObject = (JSONObject) feature;
                        JSONObject properties = featureObject.getJSONObject("properties");
                        // 判断状态导出测量数据或标注数据
                        // 用户手工绘制的json
                        if (status == 0) {
                            if ("Draw".equals(properties.get("annotation_type"))) {
                                featuresList.add(featureObject);
                            }
                        } else if (status == 1) {
                            // 导出标注数据
                            String measure = properties.getString("measure_name");
                            if (!Objects.equals(measure, "CT") && !Objects.equals(measure, "AN")) {
                                featuresList.add(featureObject);
                            }
                        }
                    }
                }
            }
        }
        // Fastjson循环引用
        parse.put("features", featuresList);
        parse.put("type", "FeatureCollection");
        //获取主要数据
        return parse;
    }

    /**
     * json 转 excel
     * @throws IOException
     */
    /**
     * json 转 excel
     *
     * @param jsonArray
     * @return
     * @throws IOException
     */
    public static HSSFWorkbook jsonToExcel(JSONArray jsonArray) {
        Set<String> keys;
        // 创建HSSFWorkbook对象
        HSSFWorkbook wb = new HSSFWorkbook();
        // 创建HSSFSheet对象
        HSSFSheet sheet = wb.createSheet("sheet0");

        String str = null;
        int roleNo = 0;
        int rowNo = 0;
        List<JSONObject> jsonObjects = jsonArray.toJavaList(JSONObject.class);

        // 创建HSSFRow对象
        HSSFRow row = sheet.createRow(roleNo++);
        // 创建HSSFCell对象
        //标题
        keys = jsonObjects.get(0).keySet();
        for (String s : keys) {
            HSSFCell cell = row.createCell(rowNo++);
            cell.setCellValue(s);
        }
        rowNo = 0;
        for (JSONObject jsonObject : jsonObjects) {
            row = sheet.createRow(roleNo++);
            for (String s : keys) {
                HSSFCell cell = row.createCell(rowNo++);
                cell.setCellValue(jsonObject.getString(s));
            }
            rowNo = 0;
        }
        System.out.println(wb);
        return wb;
    }

    /**
     * 判断文件是否有扩展名
     *
     * @param fileName 文件名
     * @return
     */
    public static boolean hasExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 && dotIndex < fileName.length() - 1;
    }



    /**
     * 递归删除指定的文件夹（包括该文件夹下的所有子文件夹和文件）.
     *
     * @param folder 要删除的文件夹
     * @return 如果文件夹删除成功，则返回true；否则返回false
     */
    public static boolean deleteFolder(File folder) {
        // 检查文件夹是否存在，如果不存在，说明删除成功；如果存在，继续删除子文件夹和文件
        if (!folder.exists()) {
            return true;
        }

        // 如果文件夹是一个文件，直接删除它
        if (folder.isFile()) {
            return folder.delete();
        }

        // 如果文件夹是一个目录，遍历其下的所有子文件夹和文件，并递归地调用删除本身的方法
        boolean result = true; // 定义布尔变量，用于判断删除是否成功
        File[] files = folder.listFiles(); // 获取文件夹中的所有文件和子文件夹
        if (files != null) {
            for (File f : files) { // 遍历每个文件或子文件夹
                if (!deleteFolder(f)) { // 对文件夹进行递归删除
                    result = false; // 如果删除失败，设置布尔变量为false
                }
            }
        }

        // 删除文件夹本身
        if (!folder.delete()) {
            result = false;
        }

        return result;
    }
}
