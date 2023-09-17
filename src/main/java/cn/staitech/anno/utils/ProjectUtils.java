package cn.staitech.anno.utils;

import cn.staitech.anno.domain.*;
import cn.staitech.anno.domain.vo.*;
import cn.staitech.anno.service.*;
import cn.staitech.common.security.utils.SecurityUtils;
import com.alibaba.fastjson.JSONObject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

import static cn.staitech.anno.constant.ProjectConstant.*;
import static cn.staitech.anno.constant.ProjectConstant.CHILDREN_CNTS;
import static cn.staitech.anno.utils.MarkVerify.wktReader;

/**
 * 项目工具类
 */
@Component
public class ProjectUtils {

    @Resource
    private IndicatorService indicatorService;

    @Resource
    private ProjectService projectService;

    @Resource
    private SlideService slideService;

    @Resource
    private SlideAnnotationResultService slideAnnotationResultService;

    @Resource
    private PathologicalIndicatorCategoryService pathologicalIndicatorCategoryService;

    public static ProjectUtils projectUtils;

    @PostConstruct
    public void init() {
        projectUtils = this;
        projectUtils.indicatorService = this.indicatorService;
        projectUtils.projectService = this.projectService;
        projectUtils.slideService = this.slideService;
        projectUtils.slideAnnotationResultService = this.slideAnnotationResultService;
        projectUtils.pathologicalIndicatorCategoryService = this.pathologicalIndicatorCategoryService;

    }

    /**
     * 分页
     *
     * @param projectInforImageVO
     * @return
     */
    public static ProjectDelVO paging(ProjectInforImageVO projectInforImageVO) {
        int pageNum = projectInforImageVO.getPageNum();
        int pageSize = projectInforImageVO.getPageSize();
        boolean flag = false;
        if (pageNum > 0) {
            pageNum--;
            flag = true;
        }
        List<ProjectListOutVO> result = new ArrayList<>();
        ProjectDelVO projectDelVO = new ProjectDelVO();
        projectDelVO.setPageNum(pageNum);
        projectDelVO.setPageSize(pageSize);
        projectDelVO.setFlag(flag);
        projectDelVO.setResult(result);
        return projectDelVO;
    }

    /**
     * 更新项目中的更新时间
     *
     * @param projectId
     */
    public static void updateProjectStatus(Long projectId) {
        ProjectStatusVO projectStatusVO = new ProjectStatusVO();
        projectStatusVO.setProjectId(projectId);
        projectStatusVO.setUpdateBy(SecurityUtils.getUserId());
        projectUtils.projectService.updateProjectStatus(projectStatusVO);
//        CacheUtils.ProjectCache(new Project());
//        return 0;
    }

    /**
     * 更新切片时间
     *
     * @param slideId
     */
    public static void updateSlideTime(Long slideId) {
        Slide slide = new Slide();
        slide.setSlideId(slideId);
        slide.setUpdateBy(SecurityUtils.getUserId());
        projectUtils.slideService.updateSlideTime(slide);
    }

    /**
     * 通过项目查询标注类别
     *
     * @param projectId
     * @param map        标注类别ID：标注类别名称
     * @param mapCreated 标注类别ID：标注类别创建者
     */
    public static void queryCategoryByProjectId(Long projectId, HashMap<Long, String> map, HashMap<Long, Long> mapCreated) {
        List<PathologicalIndicatorCategory> categoryList = projectUtils.pathologicalIndicatorCategoryService.selectCategoryByProjectId(projectId);
        categoryList.forEach(category -> {
            map.put(category.getCategoryId(), category.getCategoryName());
            mapCreated.put(category.getCategoryId(), category.getCreateBy());
        });
    }

    /**
     * 格式化X Y
     *
     * @param jsonObject
     * @return
     */
    public static StringBuilder formatXY(JSONObject jsonObject) {
        String replaceX = jsonObject.getString(ALL_POINTS_X).replace("[", "").replace("]", "");
        String replaceY = jsonObject.getString(ALL_POINTS_Y).replace("[", "").replace("]", "");
        String[] splitX = replaceX.split(",");
        String[] splitY = replaceY.split(",");
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < splitX.length; j++) {
            double parseDouble = Double.parseDouble(splitY[j]);
            double v = 0 - parseDouble;
            String y = String.valueOf(v);
            if (j == splitX.length - 1) {
                builder.append(splitX[j]).append(" ").append(y);
            } else {
                builder.append(splitX[j]).append(" ").append(y).append(",");
            }
        }
        return builder;
    }

    /**
     * 更新结果表的人工标注数
     *
     * @param slideAnnotationResult
     */
    public static void updateResultQuantity(SlideAnnotationResult slideAnnotationResult) {
        // 通过切片ID、更新者ID、标注类别查询
        SlideAnnotationResult result = projectUtils.slideAnnotationResultService.selectById(slideAnnotationResult);

        // 根据切片ID、更新者ID、标注类别统计标注数量
        Integer humanAnnotationQuantity = projectUtils.slideAnnotationResultService.selectSum(slideAnnotationResult);
        slideAnnotationResult.setSum(humanAnnotationQuantity);
        slideAnnotationResult.setProcessFlag(1);
        if (ObjectUtils.isEmpty(result)) {
            projectUtils.slideAnnotationResultService.insertBatch(slideAnnotationResult);
        } else {
            projectUtils.slideAnnotationResultService.updateBatch(slideAnnotationResult);
        }
    }

    /**
     * 更新切片、项目的人工标注数
     *
     * @param slideAnnotationResult
     */
    public static void updateHumanAnnotationQuantity(SlideAnnotationResult slideAnnotationResult) {
        Slide slide = projectUtils.slideService.selectById(slideAnnotationResult.getSlideId());

        // 通过切片ID统计人工标注数(不包含unable)
        Integer sum = projectUtils.slideAnnotationResultService.selectSumBySlideId(slideAnnotationResult.getSlideId());
        ExaminationListVO examinationListVo = new ExaminationListVO();
        examinationListVo.setSlideId(slideAnnotationResult.getSlideId());
        examinationListVo.setHumanAnnotationTotal(sum);
        examinationListVo.setProcessFlag(1);

        // 更新切片表人工标注数及标注状态
        projectUtils.slideService.updateSlideHumanAnnotationQuantity(examinationListVo);

        // 根据项目ID统计项目标注数量
        ProjectAnnotationVO annotationVO = projectUtils.slideAnnotationResultService.selectProjectHumanAnnotationQuantity(slide.getProjectId());

        //更新项目人工标注数
        projectUtils.projectService.updateProjectHumanAnnotationQuantity(annotationVO);
        CacheUtils.ProjectCache(new Project());
    }

    /**
     * 转换数据格式
     * */
    public static JSONObject jsonExportMethod(List<Annotation> annotationList, String imageName, String size, Project project1) throws ParseException {
        JSONObject jsonObject = new JSONObject();

        //_via_settings 中的字段数据
        JSONObject viaSettings = new JSONObject();
        jsonObject.put("_via_settings", viaSettings);
        JSONObject ui = new JSONObject();
        viaSettings.put("ui", ui);
        ui.put("annotation_editor_height", null);
        ui.put("annotation_editor_fontsize", null);
        ui.put("leftsidebar_width", null);
        JSONObject imageGrid = new JSONObject();
        ui.put("image_grid", imageGrid);
        imageGrid.put("img_height", null);
        imageGrid.put("rshape_fill", null);
        imageGrid.put("rshape_fill_opacity", null);
        imageGrid.put("rshape_stroke", null);
        imageGrid.put("rshape_stroke_width", null);
        imageGrid.put("show_region_shape", null);
        imageGrid.put("show_image_policy", null);

        JSONObject image = new JSONObject();
        ui.put("image", image);
        image.put("region_label", null);
        image.put("region_label_font", null);

        JSONObject core = new JSONObject();
        viaSettings.put("core", core);
        core.put("buffer_size", null);
        JSONObject filePath = new JSONObject();
        core.put("file_path", filePath);
        core.put("default_filepath", null);

        JSONObject project = new JSONObject();
        viaSettings.put("project", project);
        project.put(NAME, project1.getProjectName());

        //_via_attributes中的字段
        JSONObject viaAttributes = new JSONObject();
        jsonObject.put("_via_attributes", viaAttributes);
        JSONObject region = new JSONObject();
        viaAttributes.put("region", region);
        JSONObject boneMarrow = new JSONObject();
        region.put(BONE_MARROW, boneMarrow);
        boneMarrow.put("type", null);

        JSONObject attributesFile = new JSONObject();
        viaAttributes.put("file", attributesFile);

        //_via_img_metadata中的字段
        JSONObject viaImgMetadata = new JSONObject();
        jsonObject.put(VIA_IMG_METADATA, viaImgMetadata);

        JSONObject imageInfo = new JSONObject();
        viaImgMetadata.put(imageName + size, imageInfo);
        imageInfo.put(FILENAME, imageName);
        imageInfo.put("size", size);
        ArrayList<JSONObject> coordinate = new ArrayList<>();
        imageInfo.put(REGIONS, coordinate);


        for (Annotation annotation : annotationList) {
            ArrayList<Float> listX = new ArrayList<>();
            ArrayList<Float> listY = new ArrayList<>();
            ArrayList<JSONObject> childrenList = new ArrayList<>();


            JSONObject regionOne = new JSONObject();
            JSONObject shapeAttributes = new JSONObject();
            JSONObject regionAttributes = new JSONObject();


            Geometry geometry = wktReader.read(annotation.getLocation().trim());
            if (Objects.equals(geometry.getGeometryType(), "Point") || Objects.equals(geometry.getGeometryType(), "LineString")) {
                for (Coordinate c : geometry.getCoordinates()) {
                    listX.add((float) c.x);
                    listY.add(0 - (float) c.y);
                }
            }
            if (Objects.equals(geometry.getGeometryType(), "Polygon")) {
                //如果是嵌套
                if (annotation.getLocation().contains("),")) {
                    //外层
                    String[] split = annotation.getLocation().split("\\(")[2].split("\\),")[0].split(",");
                    for (int j = 0; j < split.length; j++) {
                        String s1 = annotation.getLocation().split("\\(")[2].split("\\),")[0].split(",")[j].split(" ")[0];
                        listX.add(new Float(s1));

                        String s2 = annotation.getLocation().split("\\(")[2].split("\\),")[0].split(",")[j].split(" ")[1];
                        listY.add(0 - new Float(s2));
                    }
                    //内层
                    for (int s = 1; s < annotation.getLocation().split(",\\(").length; s++) {
                        JSONObject childrenDict = new JSONObject();
                        ArrayList<Float> ChildrenX = new ArrayList<>();
                        ArrayList<Float> ChildrenY = new ArrayList<>();

                        String[] split1 = annotation.getLocation().split(",\\(")[s].split("\\)")[0].split(",");
                        for (int g = 0; g < split1.length; g++) {
                            String s1 = annotation.getLocation().split(",\\(")[s].split("\\)")[0].split(",")[g].split(" ")[0];
                            ChildrenX.add(new Float(s1));

                            String s2 = annotation.getLocation().split(",\\(")[s].split("\\)")[0].split(",")[g].split(" ")[1];
                            ChildrenY.add(0 - new Float(s2));
                        }
                        childrenDict.put(ALL_POINTS_X, ChildrenX);
                        childrenDict.put(ALL_POINTS_Y,  ChildrenY);
                        childrenList.add(childrenDict);
                    }
                } else {
                    for (Coordinate c : geometry.getCoordinates()) {
                        listX.add((float) c.x);
                        listY.add(0 - (float) c.y);
                    }
                }
            }
            coordinate.add(regionOne);
            regionOne.put(SHAPE_ATTRIBUTES, shapeAttributes);
            regionOne.put(REGION_ATTRIBUTES, regionAttributes);
            if (annotation.getCategoryId() == 0) {
                regionAttributes.put(BONE_MARROW, NONE);
            } else {
                //获取标注类别
                PathologicalIndicatorCategory category =  projectUtils.pathologicalIndicatorCategoryService.selectByPrimaryKey(
                        annotation.getCategoryId());
                regionAttributes.put(BONE_MARROW, category.getCategoryName());
            }
            if (annotation.getLocation().contains("),")) {
                shapeAttributes.put(NAME, POLYGON_WITH_HOLES);
            } else {
                shapeAttributes.put(NAME, annotation.getLocation().split("\\(")[0].toLowerCase(Locale.ROOT));
            }
            shapeAttributes.put("anno_id", annotation.getAnnotationId());
            shapeAttributes.put(ALL_POINTS_X, listX);
            shapeAttributes.put(ALL_POINTS_Y, listY);
            if (annotation.getLocation().contains("),")) {
                shapeAttributes.put(CHILDREN_CNTS, childrenList);
            }
        }
        return jsonObject;

    }



}
