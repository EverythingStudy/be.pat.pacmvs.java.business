package cn.staitech.anno.service.impl;

import cn.hutool.core.text.csv.*;
import cn.hutool.core.util.CharsetUtil;
import cn.staitech.anno.service.CSVParserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: wangfeng
 * @create: 2023-09-13 09:19:59
 * @Description: CSVParserServiceImpl
 */
@Slf4j
@Service
public class CSVParserServiceImpl implements CSVParserService {
    @Override
    public List<CsvRow> parser(String filePath) {
        File file = new File(filePath);
        System.out.println("file.getName() = " + file.getName());
        System.out.println("file.length() = " + file.length());


        // 参考：https://blog.csdn.net/Jeck_wu/article/details/128204519
        final CsvReader reader = CsvUtil.getReader();

        //2. 进行配置
        CsvReadConfig csvReadConfig = new CsvReadConfig();
        // 是否跳过空白行
        csvReadConfig.setSkipEmptyRows(true);
        // 是否设置首行为标题行
        csvReadConfig.setContainsHeader(true);
        //构建 CsvReader 对象
        CsvReader csvReader = CsvUtil.getReader(csvReadConfig);
        // 这里转了下 可能会产生临时文件，临时文件目录可以设置，也可以立马删除
        CsvData read = csvReader.read(file, CharsetUtil.CHARSET_GBK);
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<String> header = read.getHeader();
        List<CsvRow> rows = read.getRows();
        for (CsvRow row : rows) {
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < row.size(); i++) {
                map.put(header.get(i), row.get(i));
                // log.info("====>{} {}",header.get(i), row.get(i));
            }
            // 具体业务根据需求来就行了
/*            if (!ObjectUtils.isEmpty(map.get("mlos单号"))) {
                mapList.add(map);
            }*/
        }

        log.info("rows ====>{} {}", rows);

        return rows;

/*        mapList.forEach((k, v) -> {
            System.out.println("v = " + v);
        });*/

        // return mapList;


       /* //假设csv文件在classpath目录下
        final List<TILockdataMidModel> result = reader.read(
                ResourceUtil.getUtf8Reader(filePath), TILockdataMidModel.class);
        log.info("====>{}",result.size());
        log.info("====>{}", JSON.toJSONString(result));*/
    }

    public static void read(String filePath) throws IOException {

/*


        // 第一参数：读取文件的路径 第二个参数：分隔符（不懂仔细查看引用百度百科的那段话） 第三个参数：字符集
        CsvReader csvReader = new CsvReader(filePath,Charset.forName("UTF-8"));

        // 如果你的文件没有表头，这行不用执行
        // 这行不要是为了从表头的下一行读，也就是过滤表头
        csvReader.readHeaders();

        // 读取每行的内容
        while (csvReader.readRecord()) {
            // 获取内容的两种方式
            // 1. 通过下标获取
            System.out.print(csvReader.get(0));

            // 2. 通过表头的文字获取
            System.out.println(" " + csvReader.get("年龄"));
        }*/
    }

    public static void writer(String filePath) throws IOException {

/*        // 第一参数：新生成文件的路径 第二个参数：分隔符（不懂仔细查看引用百度百科的那段话） 第三个参数：字符集
        CsvWriter csvWriter = new CsvWriter(filePath, ',', Charset.forName("UTF-8"));

        // 表头和内容
        String[] headers = {"姓名", "年龄", "性别"};
        String[] content = {"张三", "18", "男"};

        // 写表头和内容，因为csv文件中区分没有那么明确，所以都使用同一函数，写成功就行
        csvWriter.writeRecord(headers);
        csvWriter.writeRecord(content);

        // 关闭csvWriter
        csvWriter.close();*/

    }


}
