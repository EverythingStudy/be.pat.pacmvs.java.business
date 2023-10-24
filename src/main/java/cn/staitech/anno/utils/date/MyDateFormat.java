package cn.staitech.anno.utils.date;

import java.text.*;
import java.util.Date;

/**
 * @author kingtiger
 */
public class MyDateFormat extends DateFormat {
    private DateFormat dateFm = new SimpleDateFormat(DateUtils.DEFAULT_PATTERN);


    /**
     *
     */
    public MyDateFormat() {
        System.out.println("MyDateFormat 初始化");
    }


    /**
     * @param date
     * @param toAppendTo
     * @param fieldPosition
     * @return
     */
    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        return getDefaultFm().format(date, toAppendTo, fieldPosition);
    }

    /**
     * @param source
     * @param pos
     * @return
     */
    @Override
    public Date parse(String source, ParsePosition pos) {
        return getDateFmByParam(source).parse(source, pos);
    }

    /**
     * 主要还是装饰这个方法
     *
     * @param source
     * @return
     * @throws ParseException
     */
    @Override
    public Date parse(String source) throws ParseException {
        return getDateFmByParam(source).parse(source);
    }

    private DateFormat getDateFmByParam(String dateStr) {
        return DateUtils.getDateFmByParam(dateStr);
    }

    private DateFormat getDefaultFm() {
        dateFm = new SimpleDateFormat(DateUtils.DEFAULT_PATTERN);
        return dateFm;
    }

    /**
     * 这里装饰clone方法的原因是因为clone方法在jackson中也有用到
     *
     * @return
     */
    @Override
    public Object clone() {
        return new MyDateFormat();
    }

}
