package cn.staitech.anno.controller;

import cn.staitech.anno.domain.DataMigration;
import cn.staitech.anno.project.mapper.MarkingMapperV1;
import cn.staitech.anno.project.service.MarkingServiceV1;
import cn.staitech.anno.project.domain.Marking;
import cn.staitech.anno.service.DataMigrationService;
import cn.staitech.common.core.domain.R;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * @Author wudi
 * @Date 2023/11/6 14:08
 * @desc 数据迁移
 */
@Slf4j
@RestController
@RequestMapping("/data")
public class DataMigrationController {
    @Resource
    private MarkingMapperV1 markingMapperV1;
    @Autowired
    private MarkingServiceV1 markingServiceV1;

    @Autowired
    private DataMigrationService dataMigrationService;

    @GetMapping("/migration")
    public R dataMigration() {
        boolean flag = true;
        int i = 1;
        int sum = 0;
        while (flag) {
            Page<Marking> page = PageHelper.startPage(i, 1000);
            List<Marking> markings = markingMapperV1.selectMarkings();
            if (markings.size() > 0) {
                markingServiceV1.saveBatch(markings);
                sum += markings.size();
                i++;
            } else {
                break;
            }

        }

        return R.ok(sum);

    }

    /**
     * image表数据迁移
     *
     * @return
     */
    @GetMapping("/imageData")
    public R imageData() {
        DataMigration dataMigration = new DataMigration();
        dataMigration.setImageData(dataMigrationService.imageData());
        dataMigration.setAssessmentData(dataMigrationService.assessmentData());
        dataMigration.setAlgorithmJsonData(dataMigrationService.algorithmJsonData());
        dataMigration.setExamineScoreData(dataMigrationService.examineScoreData());
        dataMigration.setFilesData(dataMigrationService.filesData());
        dataMigration.setQuestionBankData(dataMigrationService.questionBankData());
        dataMigration.setRecentlyVisitedData(dataMigrationService.recentlyVisitedData());

        return R.ok(dataMigration);
    }

    @GetMapping("/markingData")
    public R markingData() {


        return R.ok(dataMigrationService.markingData());
    }

    @GetMapping("/otherMarking")
    public R otherMarking() {

        DataMigration dataMigration = dataMigrationService.otherMarking();


        return R.ok(dataMigration);
    }
}
