package cn.staitech.anno.controller;

import cn.staitech.anno.service.CsvParserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    CsvParserService csvParserService;

/*
    @PostMapping("/get")
    public R<List<CsvRow>> getDemo() {
        String filePath = "D:\\pat2.0saas\\切片信息1.csv";
        List<CsvRow> list = csvParserService.parser(filePath);
        log.info("list ====>{}", list);
        return R.ok(list);
    }*/
}
