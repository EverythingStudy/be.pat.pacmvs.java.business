package cn.staitech.anno.domain;

import lombok.Data;

/**
 * @Author wudi
 * @Date 2023/11/10 9:29
 * @desc 数据迁移
 */
@Data
public class DataMigration {
    private int imageData;
    private int assessmentData;
    private int algorithmJsonData;
    private int examineScoreData;
    private int filesData;
    private int questionBankData;
    private int recentlyVisitedData;

}
