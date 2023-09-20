package cn.staitech.anno.service.impl;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.csv.CsvReadConfig;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.staitech.anno.service.CsvParserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: wangfeng
 * @create: 2023-09-13 09:19:59
 * @Description: CsvParserServiceImpl
 * 参考：
 * https://blog.csdn.net/weixin_41522735/article/details/111567891
 * https://blog.csdn.net/Jeck_wu/article/details/128204519
 */
@Slf4j
@Service
public class CsvParserServiceImpl implements CsvParserService {
    @Override
    public <T> List<T> read(String filePath, Class<T> clazz) {
        // 配置
        CsvReadConfig csvReadConfig = new CsvReadConfig();
        // 是否跳过空白行
        csvReadConfig.setSkipEmptyRows(true);
        // 是否设置首行为标题行
        csvReadConfig.setContainsHeader(true);
        // 构建 CsvReader 对象
        CsvReader csvReader = CsvUtil.getReader(csvReadConfig);
        // 直接使用utf8编码：ResourceUtil.getUtf8Reader(f.getPath())
        // 使用GBK编码
        List<T> result = csvReader.read(ResourceUtil.getReader(filePath, CharsetUtil.CHARSET_GBK), clazz);
        return result;
    }
}
