package cn.staitech.anno.utils.date;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * @author xiejin
 * @date 2018/6/4 14:37
 */
@Slf4j
public class DateConvert implements Converter<String, Date> {

    @Override
    public Date convert(String stringDate) {
        if (StringUtils.isBlank(stringDate)) {
            return null;
        }
        log.info("日期参数:" + stringDate);
        DateFormat fm = DateUtils.getDateFmByParam(stringDate);
        //解析
        try {
            //把字符串解析成一个日期对象
            Date parse = fm.parse(stringDate);
            //返回结果
            return parse;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
