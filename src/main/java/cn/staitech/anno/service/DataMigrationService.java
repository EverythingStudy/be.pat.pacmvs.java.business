package cn.staitech.anno.service;

import cn.staitech.anno.domain.DataMigration;

/**
 * @Author wudi
 * @Date 2023/11/8 19:19
 * @desc 数据迁移
 */
public interface DataMigrationService {
    int imageData();

    int assessmentData();

    int algorithmJsonData();

    int examineScoreData();

    int filesData();

    int markingData();

    int questionBankData();

    int recentlyVisitedData();

    DataMigration otherMarking();

}
