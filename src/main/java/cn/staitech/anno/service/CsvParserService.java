package cn.staitech.anno.service;

import java.util.List;

/**
 * @author: wangfeng
 * @create: 2023-09-13 09:19:14
 * @Description: CSV文件解析
 */

public interface CsvParserService {

    <T> List<T> read(String filePath, Class<T> clazz);
}
