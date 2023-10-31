package cn.staitech.anno.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: wangfeng
 * @create: 2023-06-21 14:28:47
 * @Description: 异步Task
 */

@Slf4j
@Service
public class AsyncTask {

    /**
     * 异步删除文件
     *
     * @param file
     * @throws InterruptedException
     */
    @Async
    public void deleteFileTask(File file) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        AtomicInteger count = new AtomicInteger(0);
        for (; ; ) {
            Thread.sleep(2);
            if (file.delete()) {
                long endTime = System.currentTimeMillis();
                log.info("[{}] async delete file success:{},cost {} ms,cas count:{}", Thread.currentThread().getName(), file.getAbsolutePath(), endTime - startTime, count.getAndIncrement());
                break;
            }
            if (count.getAndIncrement() > 30000) {
                break;
            }
        }
    }


}