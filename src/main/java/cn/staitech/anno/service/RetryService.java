package cn.staitech.anno.service;

import java.io.File;

/**
 * @author: wangfeng
 * @create: 2023-12-20 10:09:37
 * @Description: 重试服务
 */

public interface RetryService {

    boolean deleteFileRetry(File file) throws Exception;
}
