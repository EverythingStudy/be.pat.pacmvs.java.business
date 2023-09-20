package cn.staitech.anno;

import cn.staitech.anno.domain.ImageCsv;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * @author: wangfeng
 * @create: 2023-09-20 14:16:30
 * @Description: 类测试
 */

@Slf4j
public class ClazzTest {

    public static void main(String[] args) {
        Field[] fields = ImageCsv.class.getFields();
        log.info("{}", fields);


/*
        // 有数据时才处理 得到类的所有field.
        List<Object[]> fields = ImageCsv.class.getFields();
        Map<Integer, Object[]> fieldsMap = new HashMap<Integer, Object[]>();
        for (Object[] objects : fields) {
            log.info("{}", objects);

*/
/*            Excel attr = (Excel) objects[1];
            Integer column = cellMap.get(attr.name());
            if (column != null)
            {
                fieldsMap.put(column, objects);
            }*//*

        }
*/


    }
}
