package cn.staitech.anno.marking;

import cn.staitech.anno.StaiTechAnnoApplication;
import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.project.domain.Marking;
import cn.staitech.anno.project.mapper.MarkingMapperV1;
import cn.staitech.anno.project.service.MarkingServiceV1;
import cn.staitech.anno.service.DataMigrationService;
import cn.staitech.anno.service.SlideService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: wangfeng
 * @create: 2023-11-17 13:13:15
 * @Description:
 */
@Slf4j
@SpringBootTest(classes = StaiTechAnnoApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MarkingTest {
    public static final ConcurrentHashMap<Long, ConcurrentHashMap<String, Marking>> MARKING_MAP = new ConcurrentHashMap<>();

    public static final ConcurrentHashMap<String, Marking> SUB_MARKING_MAP = new ConcurrentHashMap<>();

    private static final int MAXIMUM_CAPACITY = 1 << 30;
    @Resource
    private MarkingServiceV1 markingServiceV1;
    @Resource
    private MarkingMapperV1 markingMapperV1;
    @Resource
    private SlideService slideService;

    @Resource
    private DataMigrationService dataMigrationService;


    /**
     * ConcurrentHashMap最大容量： 1073741824 约10亿
     *
     * @param args
     */
    public static void main(String[] args) {
        // ConcurrentHashMap最大容量： 1073741824 约10亿
        System.out.println("ConcurrentHashMap.MAXIMUM_CAPACITY = " + MAXIMUM_CAPACITY);
    }

    /**
     * tb_marking数据分表迁移
     */
    @Test
    public void dataMigration() {
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
        log.info("sum：{}", sum);
    }



    @Test
    public void migration() {
        dataMigrationService.markingData();
    }


    @Test
    public void markingData() {
        boolean flag = true;
        int i = 1;
        int sum = 0;
        while (flag) {
            Page<Marking> page = PageHelper.startPage(i, 1000);
            LambdaQueryWrapper<Marking> qw = new LambdaQueryWrapper<>();
            qw.select(Marking::getMarkingId, Marking::getSlideId, Marking::getGeometry);
            List<Marking> markings = markingMapperV1.selectList(qw);
            if (markings.size() > 0) {
                markings.forEach(e -> {
                    //log.info("{} {} ", e.getSlideId(), e.getMarkingId());
                    SUB_MARKING_MAP.put(e.getMarkingId(), e);
                });
                sum += markings.size();
                i++;
            } else {
                break;
            }
            log.info("SUB_MARKING_MAP.size：{}", SUB_MARKING_MAP.size());
        }
        log.info("sum：{}", sum);
    }


    @Test
    public void selectMarkingList() {
        QueryWrapper<Marking> markingQueryWrapper = new QueryWrapper<>();
        markingQueryWrapper.select("distinct slide_id");
        markingQueryWrapper.orderByAsc("slide_id");
        List<Marking> markingList = markingServiceV1.list(markingQueryWrapper);
        log.info("markingList size: {}", markingList.size());
    }


    @Test
    public void process() {
        QueryWrapper<Slide> slideQueryWrapper = new QueryWrapper<>();
        slideQueryWrapper.select("distinct slide_id");
        slideQueryWrapper.orderByAsc("slide_id");
        List<Slide> slideList = slideService.list(slideQueryWrapper);
        Long index = 0L;
        for (Slide slide : slideList) {
            Long slideId = slide.getSlideId();

            QueryWrapper<Marking> markingQueryWrapper = new QueryWrapper<>();
            markingQueryWrapper.eq("slide_id", slideId);
            markingQueryWrapper.orderByAsc("marking_id");
            List<Marking> markingList = markingServiceV1.list(markingQueryWrapper);
            ConcurrentHashMap<String, Marking> markingMap = new ConcurrentHashMap<>();
            for (Marking marking : markingList) {
                markingMap.put(marking.getMarkingId(), marking);
            }
            MARKING_MAP.put(slideId, markingMap);
            index = index + markingList.size();
            log.info("------------------rootMapSize:{} slideId:{} markingList:{} index: {}", MARKING_MAP.size(), slide.getSlideId(), markingList.size(), index);
        }

        System.out.println("------------------MARKING_MAP size: " + MARKING_MAP.size());
    }


}
