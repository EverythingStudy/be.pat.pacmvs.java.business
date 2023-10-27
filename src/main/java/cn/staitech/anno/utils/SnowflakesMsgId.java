package cn.staitech.anno.utils;

public class SnowflakesMsgId {

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static String msgId() {
        SnowFlakeGenerateIdWorker snowFlakeGenerateIdWorker = new SnowFlakeGenerateIdWorker(0L, 0L);
        String msgId = snowFlakeGenerateIdWorker.generateNextId();
        return msgId;
    }
}
