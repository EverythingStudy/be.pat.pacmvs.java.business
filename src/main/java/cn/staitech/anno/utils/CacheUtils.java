package cn.staitech.anno.utils;

import cn.staitech.anno.config.ICache;
import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.domain.Image;
import cn.staitech.anno.domain.Indicator;
import cn.staitech.anno.domain.Project;
import cn.staitech.anno.domain.vo.ProjectListVO;
import cn.staitech.anno.service.ImageService;
import cn.staitech.anno.service.IndicatorService;
import cn.staitech.anno.service.ProjectService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: YL
 * @email: yangl@staitech.cn
 * @date: 2022/10/28 星期五 11:18
 */
//@Component
public class CacheUtils {

    public static CacheUtils cacheUtils;
    @Resource
    private ICache iCache;
    @Resource
    private ImageService imageService;
    @Resource
    private IndicatorService indicatorService;
    @Resource
    private ProjectService projectService;

    /**
     * 标注图片缓存
     *
     * @param image
     */
    public static void imagesCache(Image image) {
        cacheUtils.iCache.removeList(CommonConstant.IMAGE_CACHE_KEY);
        List<Image> images = cacheUtils.imageService.selectImageAnnotationList(image);
        Map<String, Object> imageMap = images.stream()
                .collect(Collectors.toMap(image1 -> image1.getImageId() + "", Function.identity(), (a, b) -> a));
        cacheUtils.iCache.putObjectAllToMap(CommonConstant.IMAGE_CACHE_KEY, imageMap, -1);
    }

    /**
     * 病理指标缓存
     *
     * @param indicator
     */
    public static void indicatorCache(Indicator indicator) {
        cacheUtils.iCache.removeList(CommonConstant.INDICATOR_CACHE_KEY);
        List<Indicator> indicators = cacheUtils.indicatorService.selectIndicatorList1(indicator);
        Map<String, Object> indicatorMap = indicators.stream()
                .collect(Collectors.toMap(indicator1 -> indicator1.getIndicatorId() + "", Function.identity(), (a, b) -> a));
        cacheUtils.iCache.putObjectAllToMap(CommonConstant.INDICATOR_CACHE_KEY, indicatorMap, -1);
    }

    /**
     * 项目缓存
     *
     * @param project
     */
    public static void ProjectCache(Project project) {
        cacheUtils.iCache.removeList(CommonConstant.PROJECT_CACHE_KEY);
        List<ProjectListVO> projectList = cacheUtils.projectService.selectProjectList(project);
        Map<String, Object> projectMap = projectList.stream()
                .collect(Collectors.toMap(project1 -> project1.getProjectId() + "", Function.identity(), (a, b) -> a));
        cacheUtils.iCache.putObjectAllToMap(CommonConstant.PROJECT_CACHE_KEY, projectMap, -1);
    }

    /**
     * ID自增
     *
     * @param key
     * @param delta
     * @return
     */
    public static Long getAndAddLong(String key, Long delta) {
        return cacheUtils.iCache.getAndAddLong(key, delta);
    }

    // @PostConstruct
    public void init() {
        cacheUtils = this;
        cacheUtils.iCache = this.iCache;
        cacheUtils.imageService = this.imageService;
        cacheUtils.indicatorService = this.indicatorService;
        cacheUtils.projectService = this.projectService;

        /*ProjectCache(new Project());*/
        imagesCache(new Image());
        indicatorCache(new Indicator());
    }
}