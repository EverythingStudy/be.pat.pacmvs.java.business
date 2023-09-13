package cn.staitech.anno.controller;

import cn.hutool.core.text.csv.CsvRow;
import cn.staitech.anno.service.CSVParserService;
import cn.staitech.common.core.domain.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: wangfeng
 * @create: 2023-09-13 09:21:39
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/csv")
public class CSVParserController {

    @Resource
    CSVParserService csvParserService;


    @PostMapping("/get")
    public R<List<CsvRow>> getDemo() {
        String filePath = "D:\\pat2.0saas\\切片信息1.csv";
        List<CsvRow> list = csvParserService.parser(filePath);
        log.info("list ====>{}", list);
        return R.ok(list);
    }
}
