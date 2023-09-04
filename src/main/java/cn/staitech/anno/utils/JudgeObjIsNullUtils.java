package cn.staitech.anno.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
/**
 * @author 张争
 * @date 2022/12/22 14:11
 */


/**
 * <p>Description: 判断对象是否为空，进一步判断对象中的属性是否都为空 </p>
 *
 * @author crazy-water
 * @date 2018年11月12日
 */
public class JudgeObjIsNullUtils {

    /**
     * 判断对象是否为空，且对象的所有属性都为空
     * ps: boolean类型会有默认值false 判断结果不会为null 会影响判断结果
     * 序列化的默认值也会影响判断结果
     *
     * @param object
     * @return
     */
    public static boolean judgeObjIsNull(Object object) {
        Class clazz = object.getClass(); // 得到类对象
        Field fields[] = clazz.getDeclaredFields(); // 利用反射得到所有属性
        boolean flag = true; //定义标志flag
        /**
         * 循环遍历反射得到的属性数组，判断每个属性值是否为空
         */
        for (Field f : fields) {
            f.setAccessible(true);//由于考虑到某些私有属性直接访问肯能访问不到，此属性设置为true确保可以访问到
            Object fieldValue = null;
            try {
                fieldValue = f.get(object); //得到属性值
                Type fieldType = f.getGenericType();//得到属性类型
                String fieldName = f.getName(); // 得到属性名
                if (fieldName.equals("serialVersionUID")) {
                    continue;
                }
                System.out.println("属性类型：" + fieldType + ",属性名：" + fieldName + ",属性值：" + fieldValue);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (fieldValue != null) {  //只要有一个属性值不为null 就返回false 表示对象不为null
                flag = false;
                break;
            }
        }
        return flag;
    }

}

