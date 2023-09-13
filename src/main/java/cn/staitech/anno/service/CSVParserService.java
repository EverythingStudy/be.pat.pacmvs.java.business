package cn.staitech.anno.service;

import cn.hutool.core.text.csv.CsvRow;

import java.util.List;

/**
 * @author: wangfeng
 * @create: 2023-09-13 09:19:14
 * @Description: CSV文件解析
 */

public interface CSVParserService {

    List<CsvRow> parser(String filePath);
}
