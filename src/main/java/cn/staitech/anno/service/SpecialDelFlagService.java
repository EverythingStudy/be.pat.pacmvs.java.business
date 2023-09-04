package cn.staitech.anno.service;

/**
 * @author gjt.
 * @data 2023/6/8 9:27
 */
public interface SpecialDelFlagService {

    /**
     * 执行更新 将回收站的专题进行逻辑删除
     */
    void specialDelFlagExpire();
}
