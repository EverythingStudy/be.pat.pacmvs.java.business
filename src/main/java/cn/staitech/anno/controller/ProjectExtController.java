package cn.staitech.anno.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.staitech.anno.config.ICache;
import cn.staitech.anno.constant.CacheConstant;
import cn.staitech.anno.constant.ProjectConstant;
import cn.staitech.anno.constant.R.MeasureResponseConstant;
import cn.staitech.anno.constant.R.ResponseConstant;
import cn.staitech.anno.domain.*;
import cn.staitech.anno.domain.image.in.ImageAllVO;
import cn.staitech.anno.domain.image.in.ImageListVO;
import cn.staitech.anno.domain.vo.*;
import cn.staitech.anno.domain.vo.indicator.IndicatorReviseVO;
import cn.staitech.anno.domain.vo.project.InsertProjectVO;
import cn.staitech.anno.domain.vo.statistic.StatisticCategoryListOutVO;
import cn.staitech.anno.enums.ProjectImageEnum;
import cn.staitech.anno.service.*;
import cn.staitech.anno.service.impl.manage.SlideManage;
import cn.staitech.anno.utils.*;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.annotation.RequiresPermissions;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysProjectRole;
import cn.staitech.system.api.domain.SysUser;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import com.vividsolutions.jts.io.ParseException;
import io.seata.common.util.StringUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static cn.staitech.anno.constant.ProjectConstant.*;
import static cn.staitech.anno.constant.R.ExaminationResponseConstant.FILE_SUFFIX;
import static cn.staitech.anno.constant.R.MeasureResponseConstant.*;

/**
 * 项目 信息操作处理  .
 *
 * @author 作者名
 * @author 标明开发该类模块的作者
 * @version 标明该类模块的版本
 * @see ProjectController
 */
@Slf4j
@ApiIgnore
@Api(tags = "项目接口")
@RestController
@RequestMapping("/projectExt")
public class ProjectExtController extends BaseController {

    @Resource
    private ProjectService projectService;

    @Resource
    private IndicatorService indicatorService;

    @Resource
    private ImageService imageService;

    @Resource
    private PathologicalIndicatorCategoryService pathologicalIndicatorCategoryService;

    @Resource
    private GetUserInformationService getUserInformationService;

    @Resource
    private SlideService slideService;

    @Resource
    private AnnotationService annotationService;

    @Resource
    private SlideAnnotationResultService slideAnnotationResultService;

    @Resource
    private ICache iCache;

    @Resource
    private ProjectMemberService projectMemberService;

    @Resource
    private SlideManage slideManage;

    @Resource
    private ProjectRoleService projectRoleService;

    @Resource
    private ProjectMenuService projectMenuService;

    @Resource
    private ProjectRoleMenuService projectRoleMenuService;

    @Value("${jsonZipFilePath}")
    private String zipPath;

    /**
     * 修改项目描述接口
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "修改项目描述接口")
    @PostMapping("/descriptionUpdate")
    @Log(title = "项目", menu = "专题管理", subMenu = "项目管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("anno:project:description")
    public R<String> descriptionUpdate(@Validated @RequestBody ProjectDescription req) {
        Project project = new Project();
        project.setProjectId(req.getProjectId());
        project.setDescription(req.getDescription());
        int res = projectService.updateProjectDescription(project);
        projectService.selectProjectList(new Project());
        CacheUtils.ProjectCache(new Project());
        if (res > 0) {
            return R.ok("项目描述修改成功");
        } else {
            return R.fail("项目描述修改失败");
        }
    }

    /**
     * 更新更换病理前后，病理关联的数据
     */
    public int updateIndicatorMessage(PorjectVO project, ProjectListVO project1) {
        IndicatorReviseVO indicatorReviseVO = new IndicatorReviseVO();
        if (project.getIndicatorId() != null) {
            indicatorReviseVO.setIndicatorId(project.getIndicatorId().intValue());
            //更新新病理数据
            indicatorService.updateIndicator(indicatorReviseVO);
            CacheUtils.indicatorCache(new Indicator());
        }
        if (project1.getIndicatorId() != null) {
            indicatorReviseVO.setIndicatorId(project1.getIndicatorId().intValue());
            //更新原病理数据
            indicatorService.updateIndicator(indicatorReviseVO);
            CacheUtils.indicatorCache(new Indicator());
        }
        return 0;
    }

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    @ApiOperation(value = "添加项目")
    @RequiresPermissions("anno:project:addproject")
    @Log(title = "添加项目", menu = "专题管理", subMenu = "项目管理", businessType = BusinessType.INSERT)
    @PostMapping("/addProject")
    @Transactional
    public R<String> addProject(@Validated @RequestBody InsertProjectVO req) {
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        // 获取当前登录用户Id
        Long loginUser = sysUser.getUserId();
        Long organizationId = sysUser.getOrganizationId();

        String name = req.getProjectName();

        //根据项目名称查询，用来判断项目名称是否被使用
        Project projectByName = projectService.selectProjectByName(name);
        if (projectByName != null) {
            return R.fail(ProjectConstant.PROJECT_EXIST);
        }
        final Long[] imageIdList = req.getImageIdList();
        Indicator indicator = new Indicator();
        Integer status = req.getStatus();
        if (status < ProjectConstant.NOT_INDICATOR_STATUS || status > ProjectConstant.NOT_ATTRIBUTE_STATUS) {
            return R.fail(ProjectConstant.STATUS_ERROR);
        }
        Project project = new Project();
        project.setProjectName(name);
        project.setDescription(req.getDescription());
        project.setCreateBy(loginUser);
        if (status.equals(ProjectConstant.NOT_INDICATOR_STATUS)) {
            Indicator indicatorMessage = new Indicator();
            indicatorMessage.setIndicatorName(name);
            //查询病理名称是否存在，用来判断是否可以使用
            List<Indicator> indicatorList = indicatorService.selectIndicator(indicatorMessage);
            if (!indicatorList.isEmpty()) {
                return R.fail(null, ProjectConstant.INDICATOR_EXIST);
            }
            indicator.setIndicatorName(name);
            indicator.setCreateBy(loginUser);
            //添加病理
            int i = indicatorService.insertIndicator(indicator);
            if (i <= 0) {
                return R.fail(ProjectConstant.ADD_INDICATOR_ERROR);
            }
            project.setIndicatorId(Long.valueOf(indicator.getIndicatorId()));

        } else if (status.equals(ProjectConstant.INDICATOR_STATUS)) {
            project.setIndicatorId(req.getIndicatorId());
        }

        //添加项目
        int i = projectService.insertProject(project);
        // 获取当前项目Id
        Long currentProjectId = project.getProjectId();

        // 添加项目默认3个角色
        List<SysProjectRole> projectRoleList = projectRoleService.addProjectRoles(currentProjectId, loginUser);
        // 取第1条给当前用户 项目代表
        Optional<SysProjectRole> projectRole = projectRoleList.stream().findFirst();

        //添加当前项目成员
        ProjectMember projectMember = ProjectMember.builder().userId(loginUser).projectId(currentProjectId)
                .roleId(projectRole.get().getRoleId()).createBy(loginUser).build();
        projectMemberService.save(projectMember);

        // 如果当前用户非admin ,user_id =1 ,【成员配置】非admin用户创建项目时默认所属人员要添加admin为项目代表
        // http://jira.shengtong.com/browse/ANNO-708
        if (loginUser != 1L) {
            ProjectMember admin = ProjectMember.builder().userId(1L).projectId(currentProjectId)
                    .roleId(projectRole.get().getRoleId()).createBy(loginUser).build();
            projectMemberService.save(admin);
        }

        List<Long> roleList = new ArrayList<>();
        for (SysProjectRole sysProjectRole : projectRoleList) {
            if (sysProjectRole.getRoleType() == 2) {
                roleList.add(sysProjectRole.getRoleId());
            } else if (sysProjectRole.getRoleType() == 3) {
                roleList.add(sysProjectRole.getRoleId());
            }
        }
        List<ProjectMenu> projectMenuList = projectMenuService.selectList();
        for (ProjectMenu projectMenu : projectMenuList) {
            for (Long roleId : roleList) {
                ProjectRoleMenu projectRoleMenu = new ProjectRoleMenu();
                projectRoleMenu.setRoleId(roleId);
                projectRoleMenu.setMenuId(projectMenu.getMenuId());
                projectRoleMenu.setCreateBy(SecurityUtils.getUserId());
                projectRoleMenuService.insertProjectRM(projectRoleMenu);
            }
        }
        if (imageIdList.length != 0) {
            List<Slide> slideList = new ArrayList<>();
            for (Long imageId : imageIdList) {
                Slide projectImage = new Slide();
                projectImage.setProjectId(currentProjectId);
                projectImage.setImageId(imageId);
                projectImage.setCreateBy(loginUser);
                slideList.add(projectImage);
            }
            //添加图片
            slideManage.insertProjectImage(slideList, currentProjectId);
        }
        if (i > 0) {
            if (!status.equals(ProjectConstant.NOT_ATTRIBUTE_STATUS)) {
                IndicatorReviseVO indicatorReviseVO = new IndicatorReviseVO();
                indicatorReviseVO.setIndicatorId(project.getIndicatorId().intValue());
                //更新病理数据
                indicatorService.updateIndicator(indicatorReviseVO);
                CacheUtils.indicatorCache(new Indicator());
            }
            //刷新项目缓存
            CacheUtils.ProjectCache(new Project());
            return R.ok(null, ResponseConstant.OPERATE_SUCCEED);
        }
        return R.fail(ResponseConstant.OPERATE_ERROR);
    }

    /**
     * 获取项目列表 .
     */
    @ApiOperation(value = "获取项目列表接口")
    @RequiresPermissions("anno:project:lists")
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前记录起始索引", dataTypeClass = Integer.class, paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", dataTypeClass = Integer.class, paramType = "query", example = "10")})
    public R<PageMaster<ProjectListVO>> list(Project project) {
        Set<Map.Entry<Object, Object>> allFromMap = iCache.getAllFromMap(CacheConstant.PROJECT_CACHE_KEY);
        if (allFromMap.isEmpty()) {
            CacheUtils.ProjectCache(project);
            return R.ok();
        }

        Long userId = SecurityUtils.getUserId();
        List<Long> projectIdList = new ArrayList<>();
        List<ProjectMember> projectMemberList = null;
        if (userId != 1) {
            projectMemberList = projectMemberService.selectByUserId(userId);
            for (ProjectMember projectMember : projectMemberList) {
                projectIdList.add(projectMember.getProjectId());
            }
        }

        startPage();

        Page<Object> localPage = PageMethod.getLocalPage();
        int pageNum = localPage.getPageNum();
        int pageSize = localPage.getPageSize();

        List<ProjectListVO> collect = allFromMap.stream().map(e -> {
            Object key = e.getKey();
            ProjectListVO value = (ProjectListVO) e.getValue();
            return value;
        }).filter(i -> {
            if (SecurityUtils.getUserId() != 1) {
                if (projectIdList.contains(i.getProjectId())) {
                    return true;
                }
                return false;
            }
            return true;
        }).filter(i -> {
            if (StringUtils.isNotBlank(project.getProjectName())) {
                return i.getProjectName().contains(project.getProjectName());
            }
            return true;
        }).filter(i -> {
            if (Objects.nonNull(project.getIndicatorId())) {
                return StringUtils.equalsIgnoreCase(i.getIndicatorId() + "", project.getIndicatorId() + "");
            }
            return true;
        }).filter(i -> {
            if (StringUtils.isNotBlank(project.getDescription())) {
                return (i.getDescription() + "").contains((project.getDescription()) + "");
            }
            return true;
        }).filter(i -> {
            if (Objects.nonNull(project.getCreateBy())) {
                return (i.getCreateBy() + "").equals((project.getCreateBy()) + "");
            }
            return true;
        }).filter(i -> {
            Map<String, Object> params = project.getParams();
            Object beginTime = params.get("beginTime");
            if (ObjectUtil.isNotEmpty(beginTime)) {
                DateTime parse = DateUtil.parse(beginTime.toString(), "yyyy-MM-dd");
                DateTime dateTime = DateUtil.beginOfDay(parse);
                return dateTime.getTime() < i.getCreateTime().getTime();
            }
            return true;
        }).filter(i -> {
            Map<String, Object> params = project.getParams();
            Object endTime = params.get("endTime");
            if (ObjectUtil.isNotEmpty(endTime)) {
                DateTime parse = DateUtil.parse(endTime.toString(), "yyyy-MM-dd");
                DateTime dateTime = DateUtil.endOfDay(parse);
                return dateTime.getTime() > i.getCreateTime().getTime();
            }
            return true;
        }).sorted(Comparator.comparing(ProjectListVO::getUpdateTime).reversed()).collect(Collectors.toList());

        List<ProjectListVO> projectList = collect.stream().skip((long) (pageNum - 1) * pageSize).limit(pageSize)
                .collect(Collectors.toList());
        for (ProjectListVO projectListVO : projectList) {
            ProjectMember projectMember = projectMemberService.getLoginUserProjectRoleType(
                    projectListVO.getProjectId());
            if (projectMember == null) {
                projectListVO.setRoleType(null);
            } else {
                //  projectListVO.setRoleType(projectMember.getRoleType());
            }
        }
        //根据条件查询项目
        PageMaster<ProjectListVO> pageMaster = new PageMaster<>(projectList);
        pageMaster.setPageSize(pageSize);
        pageMaster.setTotal(collect.size());
        return R.ok(pageMaster);
    }

    /**
     * 查看项目详情接口 .
     */
    @ApiOperation(value = "查询项目详情接口")
    @RequiresPermissions("anno:project:query")
    @GetMapping(value = "/{projectId}")
    public R<ProjectListVO> getInfo(@PathVariable Long projectId) {

        //获取项目详情
        ProjectListVO list = projectService.selectProjectById(projectId);
        if (list == null) {
            return R.fail("projectId不存在");
        }
        // 项目用户map
        List<Map<String, Object>> userNames = new ArrayList<>();
        List<ProjectMember> projectMemberList = projectMemberService.selectByProjectId(projectId);
        List<String> userNameList = new ArrayList<>();
        for (ProjectMember projectMember : projectMemberList) {
            Map<String, Object> userMap = new HashMap<>();
            Long userId = projectMember.getUserId();
            String userName = getUserInformationService.selectById(userId).getUserName();

            userNameList.add(userName);
            userMap.put("userId", userId);
            userMap.put("userName", userName);
            //userMap.put("roleName", ProjectMemberEnum.getEnumLabelByValue(projectMember.getRoleId()));
            userMap.put("roleName", projectMember.getRoleId()); // 通地roleId获取角色名称

            userNames.add(userMap);
        }
        list.setUserNames(userNameList.stream().toArray(String[]::new));
        list.setUserMap(userNames);
/*
        Long createBy = list.getCreateBy();
        if (createBy != null) {
            //获取创建者信息
            SysUser userInformation = getUserInformationService.selectById(createBy);
            String username = userInformation.getUserName();
            list.setCreateByName(username);
        }*/

        return R.ok(list);
    }

    /**
     * 获取可以添加到项目中的图片 .
     */
    @RequiresPermissions("anno:project:image")
    @ApiOperation(value = "获取可添加项目的图片列表")
    @GetMapping("/getProjectList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前记录起始索引", dataTypeClass = Integer.class, paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", dataTypeClass = Integer.class, paramType = "query", example = "10")})
    public R<PageMaster<ImageMessageVO>> listByProjectId(ImageAllVO image) {
        startPage();
        List<ImageMessageVO> imageList = projectService.getImageList(image);
        Long projectId = image.getProjectId();
        //获取已添加的切片
        List<Slide> slideList = slideService.getProjectInformation(projectId);
        List<Long> imageIds = new ArrayList<>();
        // 遍历切片列表，将图片id添加到list
        for (Slide slide : slideList) {
            imageIds.add(slide.getImageId());
        }
        //遍历图片列表，将与list中图片id相同的或不同的，exist字段赋值
        for (ImageMessageVO imageMessageVO : imageList) {
            Long imageId = imageMessageVO.getImageId();
            if (imageIds.contains(imageId)) {
                imageMessageVO.setExist(ALREADY_ADD);
            } else {
                imageMessageVO.setExist(NOT_ADDED);
            }
        }
        //分页
        PageMaster<ImageMessageVO> pageMaster = new PageMaster<>(imageList);
        return R.ok(pageMaster);

    }

    /**
     * 通过切片ID查询对应的图像列表 .
     */
    @RequiresPermissions("anno:annotation:image")
    @ApiOperation(value = "通过切片ID查询对应的图像列表")
    @GetMapping("/imageList")
    public R<List<ImageListVO>> listByProjectId(
            @RequestParam @ApiParam(name = "slideId", value = "切片id", required = true) Long slideId) {
        if (!Optional.ofNullable(slideId).isPresent()) {
            return R.fail(ProjectConstant.SLIDE_ID_NOT_NULL);
        }
        //获取切片信息
        Slide list = slideService.selectById(slideId);
        if (!Optional.ofNullable(list).isPresent()) {
            return R.fail(ProjectConstant.IMAGE_NOT_EXIST);
        }
        Long projectId = list.getProjectId();
        //根据项目id获取图像信息
        List<ImageListVO> image = imageService.selectImageListByPorjectId(projectId);
        return R.ok(image);
    }

    /**
     * 通过项目ID查询对应的图像列表.
     *
     * @return 结果
     */
    //    @RequiresPermissions("anno:project:picture")
    //    @ProjectRequiresPermissions("system:projectMenu:contributor")
    @ApiOperation(value = "通过项目ID查询对应的图像列表")
    @GetMapping("/projectSlideList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前记录起始索引", dataTypeClass = Integer.class, paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", dataTypeClass = Integer.class, paramType = "query", example = "10")})
    public R<PageMaster<ProjectListOutVO>> listByProjectId(@Valid ProjectInforImageVO projectInforImage)
            throws Exception {
        SlideSelectVO slideSelectVO = new SlideSelectVO();
        BeanUtils.copyProperties(projectInforImage, slideSelectVO);
        //根据条件查询对应的切片信息
        List<ProjectListOutVO> examinationListVOList = slideService.selectByStatus(slideSelectVO);
        List<ProjectListOutVO> projectListOutVOList = new ArrayList<>();
        for (ProjectListOutVO slide : examinationListVOList) {
            ProjectListOutVO projectListOutVO = new ProjectListOutVO();
            Long imageId = slide.getImageId();
            //根据图像id，查询图像信息
            Image image = imageService.selectById(imageId);
            String imageName = image.getImageName();
            String thumbUrl = image.getThumbUrl();
            Long slideId = slide.getSlideId();
            //根据切片id获取，关系表中获取对应的数据
            List<SlideAnnotationResult> slideAnnotationResults = projectService.selectMessageBySlideId(slideId);
            Map<String, Long> manualMarkingMap = new HashMap<>();
            Map<String, Long> dimensionCategoryMap = new HashMap<>();
            for (SlideAnnotationResult slideAnnotationResult : slideAnnotationResults) {
                Long sum = Long.valueOf(slideAnnotationResult.getSum());
                Long updateBy = slideAnnotationResult.getUpdateBy();
                Long categoryId;
                //关系表中categoryId为空的，设置为0.
                if (slideAnnotationResult.getCategoryId() == null) {
                    categoryId = 0L;
                } else {
                    categoryId = slideAnnotationResult.getCategoryId();
                }
                String userName = getUserInformationService.selectById(updateBy).getUserName();
                String categoryName = "";
                if (categoryId == 0L) {
                    categoryName = ProjectConstant.NO_ATTRIBUTE;
                } else {
                    //获取标注类别名称
                    try {
                        categoryName = pathologicalIndicatorCategoryService.selectByPrimaryKey(categoryId)
                                .getCategoryName();
                    } catch (Exception e) {
                        continue;
                    }
                }
                if (categoryId != 1L) {
                    if (!manualMarkingMap.containsKey(userName)) {
                        manualMarkingMap.put(userName, sum);
                    } else {
                        Long oldSum = manualMarkingMap.get(userName);
                        //对用户标注数进行累计
                        manualMarkingMap.put(userName, oldSum + sum);
                    }
                    if (!dimensionCategoryMap.containsKey(categoryName)) {
                        dimensionCategoryMap.put(categoryName, sum);
                    } else {
                        Long oldSum = dimensionCategoryMap.get(categoryName);
                        //对标注类别标注数进行累计
                        dimensionCategoryMap.put(categoryName, oldSum + sum);
                    }
                }
            }
            //删除dimensionCategoryMap中value为0的值
            dimensionCategoryMap.values().removeIf(f -> f == 0f);
            //删除 manualMarkingMap中value为0的值
            manualMarkingMap.values().removeIf(f -> f == 0f);
            List<Map<String, String>> manualMarkingList = transformation(manualMarkingMap);
            List<Map<String, String>> dimensionCategoryList = transformation(dimensionCategoryMap);
            Integer processFlag = slide.getProcessFlag();
            //获取状态名称
            String enumStatusByValue = ProjectImageEnum.getEnumStatusByValue(processFlag);
            String description = slide.getDescription();

            //查询每个图片的人工标注总数
            projectListOutVO.setSlideId(slideId);
            projectListOutVO.setImageName(imageName);
            projectListOutVO.setThumbUrl(thumbUrl);
            projectListOutVO.setManualMarkingList(manualMarkingList);
            projectListOutVO.setDimensionCategoryList(dimensionCategoryList);
            projectListOutVO.setDimensionStatus(enumStatusByValue);
            projectListOutVO.setDescription(description);
            projectListOutVO.setCreateTime(slide.getCreateTime());
            projectListOutVO.setHumanAnnotationTotal(slide.getHumanAnnotationTotal());
            projectListOutVO.setUpdateTime(slide.getUpdateTime());
            projectListOutVOList.add(projectListOutVO);
        }
        Integer taggerId = projectInforImage.getTaggerId();
        Integer categoryId = projectInforImage.getCategoryId();
        // 判断taggerId,categoryId是否为空
        if (taggerId != null || categoryId != null) {
            for (ProjectListOutVO i : new ArrayList<>(projectListOutVOList)) {

                ProjectInforImageVO projectInforImageVO = new ProjectInforImageVO();
                projectInforImageVO.setProjectId(projectInforImage.getProjectId());
                if (taggerId != null) {
                    projectInforImageVO.setCreateBy(projectInforImage.getTaggerId().longValue());
                }
                if (categoryId != null) {
                    projectInforImageVO.setCategoryId(projectInforImage.getCategoryId());
                }
                //根据条件查询符合的slideId
                List<AnnotationsAddVO> slideIdList = projectService.selectSlideId(projectInforImageVO);
                int num = 0;
                for (AnnotationsAddVO addVO : slideIdList) {
                    // 传入切片id与项目id和用户id查询出的进行对比
                    if (Objects.equals(addVO.getSlideId(), i.getSlideId())) {
                        num++;
                    }
                }
                if (num < 1) {
                    //删除不符合的切片
                    projectListOutVOList.remove(i);
                }
            }
        }

        ProjectDelVO projectDelVO = ProjectUtils.paging(projectInforImage);
        int pageSize = projectDelVO.getPageSize();
        int pageNum = projectDelVO.getPageNum();
        boolean flag1 = projectDelVO.isFlag();
        List<ProjectListOutVO> result = projectDelVO.getResult();
        for (int i = pageNum * pageSize; i < pageNum * pageSize + pageSize; i++) {
            if (i < projectListOutVOList.size()) {
                result.add(projectListOutVOList.get(i));
            }
        }
        PageMaster<ProjectListOutVO> pageMaster = new PageMaster<>(result);
        if (flag1) {
            pageNum++;
        }
        pageMaster.setPageNum(pageNum);
        pageMaster.setPageSize(pageSize);
        pageMaster.setTotal(projectListOutVOList.size());
        return R.ok(pageMaster);
    }

    private List<Map<String, String>> transformation(Map<String, Long> map) {
        List<Map.Entry<String, Long>> list = new ArrayList<>(map.entrySet());
        List<Map<String, String>> resultList = new ArrayList<>();
        Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {
            @Override
            public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        for (Map.Entry<String, Long> stringLongEntry : list) {
            Map<String, String> temp = new HashMap<>();
            String key = stringLongEntry.getKey();
            Long value = stringLongEntry.getValue();
            temp.put("key", key);
            temp.put("value", value.toString());
            resultList.add(temp);
        }
        return resultList;
    }

    /**
     * 删除项目图像 .
     *
     * @return tureOrFalse
     */
    @RequiresPermissions("anno:project:removeimage")
    @Log(title = "项目图像删除", menu = "专题管理", subMenu = "项目管理", businessType = BusinessType.DELETE)
    @ApiOperation(value = "项目批量删除图片-删除切片")
    @PostMapping("/del")
    public R<String> delImage(@Validated @RequestBody SlideVO req) throws Exception {

        Long[] slideIdList = req.getSlideId();
        boolean empty = ArrayUtil.isEmpty(slideIdList);
        if (empty) {
            return R.fail(ProjectConstant.NOT_IMAGE);
        }
        //删除图片
        boolean flag = slideService.deleteProjectImage(slideIdList);

        CacheUtils.ProjectCache(new Project());

        if (flag) {
            return R.ok(ResponseConstant.OPERATE_SUCCEED);
        }
        return R.fail(ResponseConstant.OPERATE_ERROR);

    }

    /**
     * 修改项目 .
     */
    @ApiOperation(value = "修改项目接口")
    @RequiresPermissions("anno:project:edit")
    @Log(title = "修改项目", menu = "专题管理", subMenu = "项目管理", businessType = BusinessType.UPDATE)
    @PutMapping("/put")
    public R<Integer> edit(@Validated @RequestBody PorjectVO project) {
        String projectName = project.getProjectName();
        Long projectId = project.getProjectId();
        ProjectGetVO projectGetVO = new ProjectGetVO();
        projectGetVO.setProjectId(projectId);
        projectGetVO.setProjectName(projectName);
        //根据项目id查询项目信息（主要是获取修改前关联的病理id）
        ProjectListVO project1 = projectService.selectProjectById(projectId);
        if (Objects.equals(projectName, project1.getProjectName()) && Objects.equals(project1.getIndicatorId(),
                project.getIndicatorId())) {
            return R.ok(null, ProjectConstant.NOT_CHANGE);
        }
        //查询项目名称是否被使用，除了自己
        List<Project> projectByName = projectService.selectProjectName(projectGetVO);
        if (!projectByName.isEmpty()) {
            return R.fail(ProjectConstant.PROJECT_EXIST);
        }
        Project projectMessage = new Project();
        projectMessage.setProjectId(projectId);
        projectMessage.setProjectName(projectName);
        projectMessage.setUpdateBy(SecurityUtils.getUserId());
        SlideSelectVO slideSelectVO = new SlideSelectVO();
        slideSelectVO.setProjectId(projectId);
        slideSelectVO.setProcessFlag(3);
        //查询该项目下，标注已完成提交复核切片是否存在
        List<ProjectListOutVO> slideList1 = slideService.selectByStatus(slideSelectVO);
        if (!slideList1.isEmpty()) {
            if (!Objects.equals(project1.getProjectName(), projectName)) {
                if (project.getDictCode() == null) {
                    //更新项目将脏器组织改为null

                } else {
                    //更新项目名称或脏器组织
                    projectService.updateProject(projectMessage);
                }
                //更新项目缓存
                CacheUtils.ProjectCache(new Project());
                return R.ok(null, ProjectConstant.MODIFIED_SUCCESSFULLY);
            }
            return R.ok(2);
        }
        if (project.getDictCode() == null) {
            //更新项目将脏器组织改为null

        } else {
            //更新项目名称或脏器组织
            projectService.updateProject(projectMessage);
        }
        //更新项目缓存
        CacheUtils.ProjectCache(new Project());
        if (project.getIndicatorId() != null) {
            //查询病理是否存在
            Indicator indicatorData = indicatorService.selectIndicatorsById(project.getIndicatorId());
            if (indicatorData == null) {
                return R.fail(ProjectConstant.INDICATOR_DATA);
            }
            if (Objects.equals(project.getIndicatorId(), project1.getIndicatorId())) {
                return R.ok(0);
            }
        }
        //获取项目下的切片信息
        List<Slide> slideList = slideService.getProjectInformation(projectId);
        Integer quantity = 0;
        for (Slide slide : slideList) {
            Long slideId = slide.getSlideId();
            SlideCategoryProcessFlagVO slideCategoryProcessFlagVO = new SlideCategoryProcessFlagVO();
            slideCategoryProcessFlagVO.setSlideId(slideId);
            //获取标注类别不为空的，人工标注数
            Integer slideAnnotationResult = projectService.selectCategorySum(slideCategoryProcessFlagVO);
            if (slideAnnotationResult == null) {
                continue;
            }
            quantity += slideAnnotationResult;
        }
        if (quantity > 0) {
            return R.ok(1);
        }
        project.setUpdateBy(SecurityUtils.getUserId());
        Project projectUpdate = new Project();
        BeanUtils.copyProperties(project, projectUpdate);
        if (project.getIndicatorId() == null) {
            //将项目病理修改为null
            projectService.updateProjectIndicator(projectUpdate);
            CacheUtils.ProjectCache(new Project());
            CacheUtils.indicatorCache(new Indicator());
        } else {
            //修改项目名称或病理指标
            projectService.updateProject(projectUpdate);
            CacheUtils.ProjectCache(new Project());
        }
        // 更新病理表数据
        updateIndicatorMessage(project, project1);
        return R.ok(0);
    }

    /**
     * 修改项目病理 .
     */
    @ApiOperation(value = "修改项目病理")
    @RequiresPermissions("anno:project:edit")
    @Log(title = "修改项目病理", menu = "专题管理", subMenu = "项目管理", businessType = BusinessType.UPDATE)
    @PutMapping("/editIndicator")
    public R<String> editIndicator(@Validated @RequestBody ProjectEditVO project) {
        final Long projectId = project.getProjectId();
        final Long indicatorId = project.getIndicatorId();
        if (project.getIndicatorId() != null) {
            //根据病理id，查询病理信息
            Indicator indicatorData = indicatorService.selectIndicatorsById(indicatorId);
            if (indicatorData == null) {
                return R.fail(ProjectConstant.INDICATOR_DATA);
            }
        }
        //根据项目id，查询项目信息
        ProjectListVO projectInformation = projectService.selectProjectById(projectId);
        if (Objects.equals(project.getIndicatorId(), projectInformation.getIndicatorId())) {
            return R.ok(ResponseConstant.OPERATE_SUCCEED);
        }
        Project projectUpdate = new Project();
        projectUpdate.setProjectId(projectId);
        projectUpdate.setUpdateBy(SecurityUtils.getUserId());
        if (project.getIndicatorId() == null) {
            projectService.updateProjectIndicator(projectUpdate);
            CacheUtils.ProjectCache(new Project());
        } else {
            projectUpdate.setIndicatorId(indicatorId);
            //修改项目病例指标
            projectService.updateProject(projectUpdate);
            CacheUtils.ProjectCache(new Project());
        }
        PorjectVO PorjectVO = new PorjectVO();
        BeanUtils.copyProperties(project, PorjectVO);
        // 更新病理表数据
        updateIndicatorMessage(PorjectVO, projectInformation);
        //将标注中修改前和项目有关的标注类别改0
        projectService.updateCategory(projectId);
        Slide slide = new Slide();
        slide.setProjectId(projectId);
        //根据项目id查询与项目有关的信息（主要获取标注人id）
        List<Annotation> annotationNumVos = annotationService.selectByProjectId(projectId);
        //根据项目id从切片表里获取切片信息
        List<Slide> slideList = slideService.selectImageExist(slide);
        for (Slide slide1 : slideList) {
            Long slideId = slide1.getSlideId();
            //删除tb_slide_annotation_result表里，病理修改前与项目切片有关的所有信息
            projectService.deleteBySlideId(slideId);
            for (Annotation annotation : annotationNumVos) {
                if (annotation == null) {
                    continue;
                }
                Long createBy = annotation.getCreateBy();
                AnnotationsAddVO annotationsAddVO = new AnnotationsAddVO();
                annotationsAddVO.setProjectId(projectId);
                annotationsAddVO.setSlideId(slideId);
                annotationsAddVO.setCreateBy(createBy);
                annotationsAddVO.setCategoryId(0L);
                //根据项目id、切片id、创建者id 从tb_annotation表里获取信息（获取标注数）
                AnnotationsAddVO annotationsAddVO1 = projectService.selectSumByProjectId(annotationsAddVO);
                int sum = annotationsAddVO1.getSum();
                if (sum == 0) {
                    continue;
                }
                SlideAnnotationResult slideAnnotationResult = new SlideAnnotationResult();
                slideAnnotationResult.setSlideId(slideId);
                slideAnnotationResult.setProcessFlag(1);
                slideAnnotationResult.setUpdateBy(createBy);
                slideAnnotationResult.setSum(sum);
                slideAnnotationResult.setCategoryId(0L);
                //重新往tb_slide_annotation_result表里添加数据
                slideAnnotationResultService.insertBatch(slideAnnotationResult);
            }
        }
        return R.ok(ResponseConstant.OPERATE_SUCCEED);
    }

    /**
     * 删除项目接口 .
     */
    @ApiOperation(value = "删除项目接口")
    @RequiresPermissions("anno:project:remove")
    @Log(title = "项目删除", menu = "专题管理", subMenu = "项目管理", businessType = BusinessType.UPDATE)
    @PostMapping("/projectDel")
    public R<String> logicalDel(@Validated @RequestBody ProjectDelVO projectInformation) {
        Long projectId = projectInformation.getProjectId();
        //获取项目信息
        ProjectListVO projectMessage = projectService.selectProjectById(projectId);
        IndicatorReviseVO indicatorReviseVO = new IndicatorReviseVO();
        if (projectMessage.getIndicatorId() != null) {
            indicatorReviseVO.setIndicatorId(projectMessage.getIndicatorId().intValue());
        }
        //根据项目id，获取切片信息
        List<Slide> stProject = slideService.getProjectInformation(projectId);
        if (!stProject.isEmpty()) {
            for (Slide slide : stProject) {
                Long slideId = slide.getSlideId();
                //获取标注状态
                int processFlag = slideService.selectById(slideId).getProcessFlag();
                if (processFlag > 0) {
                    return R.fail(null, ProjectConstant.UNABLE_TO_DELETE);
                }
            }
            //删除项目中的图片
            slideService.deleteProjectInformation(projectId);
        }
        //删除项目
        projectService.deleteProjectById(projectId);
        iCache.removeKeysFromMap(CacheConstant.PROJECT_CACHE_KEY, new String[]{projectId + ""});
        //更新病理表数据
        if (Objects.nonNull(projectMessage.getIndicatorId())) {
            indicatorService.updateIndicator(indicatorReviseVO);
            CacheUtils.indicatorCache(new Indicator());
        }
        return R.ok(null, ResponseConstant.OPERATE_SUCCEED);
    }

    /**
     * 管理者下拉列表
     *
     * @return 结果
     */
    @RequiresPermissions("anno:project:manager")
    @ApiOperation(value = "管理者下拉列表接口")
    @GetMapping("/manager")
    public R<List<Project>> manager() {
        //获取所有创建者id
        List<Project> createByList = projectService.selectProjectCreateBy();
        for (Project user : createByList) {
            //根据id查询用户信息
            SysUser sysUser = getUserInformationService.selectById(user.getCreateBy());
        }
        return R.ok(createByList);
    }

    /**
     * 项目切片excel导出.
     *
     * @param projectInforImageVO 查询条件
     */
    @RequiresPermissions("anno:project:export")
    @ApiOperation(value = "项目切片excel导出")
    @PostMapping("/export")
    public void export(@Validated ProjectInforImageVO projectInforImageVO, HttpServletResponse response)
            throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put(IMAGE_ID, "slideId");
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put(IMAGE_NAME, "slideName");
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put(PLATFORM_NAME, "platformName");
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put(ENTRY_NAME, "projectName");
        Map<String, String> map4 = new HashMap<String, String>();
        map4.put(TOTAL_NUMBER_OF_IMAGE_ANNOTATIONS, "annotationTotal");
        List<Map<String, String>> titleList = new ArrayList<>();
        titleList.add(map);
        titleList.add(map1);
        titleList.add(map2);
        titleList.add(map3);
        titleList.add(map4);
        //项目id
        Long projectId = projectInforImageVO.getProjectId();
        ProjectInforImageVO projectInforImageVO1 = new ProjectInforImageVO();
        projectInforImageVO1.setProjectId(projectId);
        projectInforImageVO1.setImageName(projectInforImageVO.getImageName());
        projectInforImageVO1.setProcessFlag(projectInforImageVO.getProcessFlag());
        //查询项目信息
        List<ProjectListVO> projectListVOList = projectService.selectProjectDetails(projectInforImageVO1);
        //存储类别id
        List<Integer> categoryIdList = new ArrayList<>();
        //存储用户id
        List<Long> userIdList = new ArrayList<>();

        List<StatisticCategoryListOutVO> categoryList = pathologicalIndicatorCategoryService.selectByProjectId(
                projectId);
        //添加标注类别表头
        for (StatisticCategoryListOutVO category : categoryList) {
            Map<String, String> mapCategory = new HashMap<String, String>();
            if (category.getCategoryName() == null) {
                mapCategory.put("无属性数量", "0");
                titleList.add(mapCategory);
                continue;
            }
            //不显示unLabel
            if (category.getCategoryId() == 1) {
                continue;
            }
            mapCategory.put(category.getCategoryName() + NUMBER, category.getCategoryId().toString());
            titleList.add(mapCategory);
            //存储标注类别id
            categoryIdList.add(category.getCategoryId());
        }
        ProjectInforImageVO projectInforImageVO2 = new ProjectInforImageVO();
        if (projectListVOList.size() == 1) {
            projectInforImageVO2.setSlideId(projectListVOList.get(0).getSlideId());
        }
        projectInforImageVO2.setProjectId(projectId);
        List<SlideAnnotationResult> selectUpdateBy = slideAnnotationResultService.selectUpdateBy(projectInforImageVO2);
        //成员标注数量表头
        for (SlideAnnotationResult user : selectUpdateBy) {
            Map<String, String> mapUser = new HashMap<String, String>();
            SysUser sysUser = getUserInformationService.selectById(user.getUpdateBy());
            mapUser.put(MEMBER + sysUser.getUserName() + LABEL_QUANTITY, user.getUpdateBy().toString() + MEMBER);
            titleList.add(mapUser);
            //存储用户id
            userIdList.add(user.getUpdateBy());
        }
        //成员标注百分比表头
        for (SlideAnnotationResult user : selectUpdateBy) {
            Map<String, String> mapPercentage = new HashMap<String, String>();
            SysUser sysUser = getUserInformationService.selectById(user.getUpdateBy());
            mapPercentage.put(MEMBER + sysUser.getUserName() + PROPORTION_OF_MARKED_QUANTITY,
                    user.getUpdateBy().toString() + PROPORTION);
            titleList.add(mapPercentage);
        }

        //行内数据
        List<Map<String, String>> rowList = new ArrayList<>();
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        for (ProjectListVO projectListVO : projectListVOList) {
            Map m = new HashMap<String, String>();
            m.put("slideId", projectListVO.getSlideId());
            m.put("slideName", projectListVO.getImageName());
            m.put("platformName", NEW_ANNOTATION_PLATFORM);
            m.put("projectName", projectListVO.getProjectName());
            SlideCategoryProcessFlagVO slideCategoryProcessFlagVO = new SlideCategoryProcessFlagVO();
            slideCategoryProcessFlagVO.setSlideId(projectListVO.getSlideId());
            //查询切片标注总数
            Integer annotationTotal = projectService.selectCategoryTotal(slideCategoryProcessFlagVO);
            if (annotationTotal == null) {
                m.put("annotationTotal", 0);
            } else {
                m.put("annotationTotal", annotationTotal);
            }
            for (Integer category : categoryIdList) {
                SlideCategoryProcessFlagVO processFlagVO = new SlideCategoryProcessFlagVO();
                processFlagVO.setSlideId(projectListVO.getSlideId());
                processFlagVO.setCategoryId(category);
                if (projectInforImageVO.getProcessFlag() != null) {
                    processFlagVO.setProcessFlag(projectInforImageVO.getProcessFlag().longValue());
                }
                SlideAnnotationResult categorySum = projectService.selectByCategoryId(processFlagVO);
                if (categorySum == null) {
                    m.put(category.toString(), 0);
                    continue;
                }
                m.put(category.toString(), categorySum.getSum());
            }
            //查询切片下标注类别为null的标注总数
            SlideAnnotationResult unLabelNum = projectService.selectUserMessage(slideCategoryProcessFlagVO);
            if (unLabelNum == null) {
                m.put("0", 0);
            } else {
                m.put("0", unLabelNum.getSum());
            }
            //查询每个切片的总标注数
            SlideCategoryProcessFlagVO slideCategoryProcessFlagVO1 = new SlideCategoryProcessFlagVO();
            slideCategoryProcessFlagVO1.setSlideId(projectListVO.getSlideId());
            Integer totalSum = projectService.selectCategoryTotal(slideCategoryProcessFlagVO1);
            for (Long user : userIdList) {

                slideCategoryProcessFlagVO1.setUpdateBy(user.longValue());
                Integer userAnnotationNum = projectService.selectCategoryTotal(slideCategoryProcessFlagVO1);
                if (userAnnotationNum == null) {
                    m.put(user + MEMBER, 0);
                    m.put(user + PROPORTION, 0);
                    continue;
                }
                if (userAnnotationNum == 0) {
                    m.put(user + MEMBER, userAnnotationNum);
                    m.put(user + PROPORTION, 0);
                    continue;
                }
                m.put(user + MEMBER, userAnnotationNum);
                String result = numberFormat.format((float) userAnnotationNum / (float) totalSum * 100);
                m.put(user + PROPORTION, result + "%");
            }
            rowList.add(m);
        }

        ExcelTool excelTool = new ExcelTool(PROJECT_SLICE_DATA, 15, 20);
        List<Column> titleData = excelTool.columnTransformer(titleList);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        excelTool.exportExcel(titleData, rowList, response.getOutputStream(), true, false);
    }

    /**
     * 修改项目状态 .
     */
    @ApiOperation(value = "修改项目状态接口")
    @RequiresPermissions("anno:project:editstatus")
    @Log(title = "修改项目状态", menu = "专题管理", subMenu = "项目管理", businessType = BusinessType.UPDATE)
    @PutMapping("/editStatus")
    public R<String> editStatus(@Validated @RequestBody ProjectStatusVO project) {
        //查询项目信息
        ProjectListVO projectListVO = projectService.selectProjectById(project.getProjectId());
        if (projectListVO == null) {
            return R.fail("项目不存在");
        }
        //设置更新者id
        project.setUpdateBy(SecurityUtils.getUserId());
        //更新项目状态
        projectService.updateProjectStatus(project);
        //刷新项目缓存
        CacheUtils.ProjectCache(new Project());
        return R.ok(null, "修改成功");
    }

    /**
     * 获取项目下的标注类别 .
     */
    @ApiOperation(value = "获取项目下的标注类别")
    @GetMapping(value = "/categoryList")
    public R<List<PathologicalIndicatorCategory>> getCategory(
            @RequestParam @ApiParam(name = "projectId", value = "项目id", required = true) Long projectId) {
        List<PathologicalIndicatorCategory> category = pathologicalIndicatorCategoryService.selectProjectCategory(
                projectId);
        return R.ok(category);
    }

    /**
     * 获取项目下的标注类别 .
     */
    @ApiOperation(value = "获取项目标注人员")
    @GetMapping(value = "/tagger")
    public R<List<ProjectListVO>> getTagger(
            @RequestParam @ApiParam(name = "projectId", value = "项目id", required = true) Long projectId) {
        List<ProjectListVO> projectTagger = projectService.selectProjectTagger(projectId);
        return R.ok(projectTagger);
    }

    /**
     * 项目列表JSON文件导入
     *
     * @param multipartFile
     * @return
     */
    @ApiOperation(value = "项目列表JSON文件导入接口")
    @PostMapping("/upload")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "multipartFile", value = "图像标注JSON文件", required = true, dataType = "file")})
    public R<String> upload(@RequestParam("multipartFile") MultipartFile multipartFile,
                            @RequestParam(value = "projectId") @ApiParam(name = "projectId", value = "项目ID", required = true) Long projectId)
            throws IOException {
        // 参数校验
        Project project = projectService.selectPrimKey(projectId);
        if (!Optional.ofNullable(project).isPresent()) {
            return R.fail("", ARGUMENT_INVALID);
        }
        if (multipartFile.isEmpty()) {
            return R.fail("", NO_FILE);
        }

        // 获取项目下所有图像
        SlideSelectVO slideSelectVO = new SlideSelectVO();
        slideSelectVO.setProjectId(projectId);
        List<ProjectListOutVO> projectListOutVOS = slideService.selectByStatus(slideSelectVO);
        if (projectListOutVOS.isEmpty()) {
            return R.fail(IMAGE_NOT_EXIST);
        }

        // 获取原始文件名
        String originalFileName = multipartFile.getOriginalFilename();

        // 获取后缀名
        String suffixName = originalFileName.substring(originalFileName.lastIndexOf("."));

        // 将json文件转为字符串类型
        File file = new File("/" + originalFileName);

        try {
            //将MultipartFile类型转换为File类型
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
            String jsonString = FileUtils.readFileToString(file, CHARACTER_SET);

            //如果是json文件
            if (suffixName.equals(FILE_SUFFIX)) {
                //将json字符串转为实体类
                JSONObject jsonObject = JSONObject.parseObject(jsonString);
                Annotation annotation = JSONObject.toJavaObject(jsonObject, Annotation.class);
                String string = jsonObject.getString(VIA_IMG_METADATA);
                JSONObject viaImgMetadata = JSONObject.parseObject(string);
                for (Object o : viaImgMetadata.values()) {
                    // 校验图像是否存在
                    JSONObject viaImgMetadataJsonObject = (JSONObject) o;
                    String filename = viaImgMetadataJsonObject.getString(FILENAME);
                    SlideSelectVO slide = new SlideSelectVO();
                    slide.setProjectId(projectId);
                    slide.setImageName(filename);
                    List<ProjectListOutVO> slideList = slideService.selectByStatus(slide);
                    if (slideList.isEmpty()) {
                        return R.fail(IMAGE_NOT_EXIST);
                    }
                    slideList.forEach(t -> {
                        Long userId = SecurityUtils.getUserId();
                        annotation.setProjectId(projectId);
                        annotation.setSlideId(t.getSlideId());
                        annotation.setImageId(t.getImageId());
                        annotation.setCreateBy(userId);
                        annotation.setUpdateBy(userId);
                    });

                    // 校验JSON文件是否重复上传
                    List<Annotation> annotationList = annotationService.queryUploadAnnotation(annotation);
                    JSONArray regions = viaImgMetadataJsonObject.getJSONArray(REGIONS);
                    if (ObjectUtil.isNotEmpty(annotationList)) {
                        return R.fail(filename + IMAGE_UPLOADED_ANNOTATION);
                    }

                    HashMap<Long, String> mapCategoryName = new HashMap<>();
                    HashMap<Long, Long> mapCreateBy = new HashMap<>();
                    // 校验标注类别
                    for (int m = 0; m < regions.size(); m++) {
                        ProjectUtils.queryCategoryByProjectId(projectId, mapCategoryName, mapCreateBy);
                        String categoryName = JSONObject.parseObject(
                                regions.getJSONObject(m).getString(REGION_ATTRIBUTES)).getString(BONE_MARROW);
                        if (!categoryName.equals(NONE) && !mapCategoryName.containsValue(categoryName)) {
                            return R.fail(ANNOTATION_CATEGORY + categoryName + NON_EXISTENT);
                        }
                    }

                    for (int i = 0; i < regions.size(); i++) {
                        //  获取标注ID
                        ProjectUtils.queryCategoryByProjectId(projectId, mapCategoryName, mapCreateBy);
                        String categoryName = JSONObject.parseObject(
                                regions.getJSONObject(i).getString(REGION_ATTRIBUTES)).getString(BONE_MARROW);
                        Long categoryId = mapCategoryName.entrySet().stream()
                                .collect(Collectors.toMap(entity -> entity.getValue(), entity -> entity.getKey()))
                                .get(categoryName);
                        Long createCategoryId = mapCreateBy.get(categoryId);
                        if (!categoryName.equals(NONE)) {
                            annotation.setCategoryId(categoryId);
                            annotation.setCreateCategoryId(createCategoryId);
                        } else {
                            annotation.setCategoryId(NOT_AUDIT);
                        }

                        // 解析X Y
                        JSONObject shapeAttributesJSONObject = JSONObject.parseObject(
                                regions.getJSONObject(i).getString(SHAPE_ATTRIBUTES));

                        // 格式化X Y
                        StringBuilder shapeAttributesFormatXY = ProjectUtils.formatXY(shapeAttributesJSONObject);
                        String locationType = shapeAttributesJSONObject.getString(NAME);

                        StringBuilder location = new StringBuilder();
                        if (locationType.equalsIgnoreCase(POLYGON)) {
                            location.append(locationType.toUpperCase()).append("((").append(shapeAttributesFormatXY)
                                    .append("))");
                        }
                        if (locationType.equalsIgnoreCase(POINT) || locationType.equalsIgnoreCase(LINESTRING)) {
                            location.append(locationType.toUpperCase()).append("(").append(shapeAttributesFormatXY)
                                    .append(")");
                        }

                        // 解析children_cnts
                        if (locationType.equalsIgnoreCase(POLYGON_WITH_HOLES)) {
                            JSONArray childrenCntsJSONArray = shapeAttributesJSONObject.getJSONArray(CHILDREN_CNTS);
                            StringBuilder childrenCnts = new StringBuilder();
                            for (int k = 0; k < childrenCntsJSONArray.size(); k++) {
                                JSONObject childrenCntsJSONObject = JSONObject.parseObject(
                                        childrenCntsJSONArray.get(k).toString());
                                // 格式化X Y
                                StringBuilder childrenCntsformatXY = ProjectUtils.formatXY(childrenCntsJSONObject);
                                childrenCnts.append("(").append(childrenCntsformatXY);
                                if (k == childrenCntsJSONArray.size() - 1) {
                                    childrenCnts.append(")");
                                } else {
                                    childrenCnts.append("),");
                                }
                            }
                            location.append(POLYGON).append("((").append(shapeAttributesFormatXY);
                            // 校验children_cnts是否为空
                            if (ObjectUtil.isEmpty(childrenCnts)) {
                                location.append("))");
                            } else {
                                location.append("),").append(childrenCnts).append(")");
                            }
                        }
                        annotation.setLocation(location.toString());

                        // 添加导入标注
                        annotationService.insertAnnotation(annotation);
                        // 更新切片、项目的人工标注数
                        SlideAnnotationResult slideAnnotationResult = new SlideAnnotationResult();
                        slideAnnotationResult.setSlideId(annotation.getSlideId());
                        slideAnnotationResult.setUpdateBy(annotation.getUpdateBy());
                        slideAnnotationResult.setCategoryId(annotation.getCategoryId());
                        ProjectUtils.updateResultQuantity(slideAnnotationResult);
                        ProjectUtils.updateHumanAnnotationQuantity(slideAnnotationResult);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(INCORRECT_FORMAT);
        } finally {
            FileUtils.delete(file);
        }
        return R.ok("", UPLOAD_SUCCESS);
    }

    /**
     * 项目下载json文件
     */
    @ApiOperation(value = "项目下载JSON文件")
    @GetMapping("/exportJson")
    public void json(
            @RequestParam(value = "projectId") @ApiParam(name = "projectId", value = "项目ID", required = true) Long projectId,
            HttpServletResponse response) {
        // 参数校验
        Project project1 = projectService.selectPrimKey(projectId);
        String projectName = project1.getProjectName();
        SlideSelectVO slideSelectVO = new SlideSelectVO();
        slideSelectVO.setProjectId(projectId);
        //获取项目信息（项目下的所有图片）
        List<ProjectListOutVO> projectListOutVOList = slideService.selectByStatus(slideSelectVO);
        try {
            File file = new File(zipPath);
            // 如果目录不存在，创建父目录
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(zipPath + File.separator + projectName + ".zip");
            ZipOutputStream zos = new ZipOutputStream(fileOutputStream);
            ByteArrayInputStream bais;

            for (ProjectListOutVO projectListOutVO : projectListOutVOList) {
                Long slideId = projectListOutVO.getSlideId();
                String imageName = projectListOutVO.getImageName();
                String size = projectListOutVO.getSize();
                // 获取当前切片下所有标注
                List<Annotation> annotationList = annotationService.selectSlideBy(slideId);
                if (annotationList.isEmpty()) {
                    continue;
                }
                //对每个图片中的数据进行转换
                JSONObject jsonObject0 = ProjectUtils.jsonExportMethod(annotationList, imageName, size, project1);
                zos.putNextEntry(new ZipEntry(imageName.split("\\.")[0] + "_" + slideId + FILE_SUFFIX));

                //json数据转为输入流
                bais = new ByteArrayInputStream(jsonObject0.toString().getBytes(CHARACTER_SET));

                int len = 0;
                byte[] buf = new byte[1024];

                //从输入流中读取数据，写入到zip输出流
                while ((len = bais.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                bais.close();
                zos.closeEntry();
            }
            zos.flush();
            zos.close();
            File zip = new File(zipPath + File.separator + projectName + ".zip");
            long zipLength = zip.length();

            // 获取文件名
            String filename = zip.getName();
            // 将文件写入输入流
            FileInputStream fileInputStream = new FileInputStream(zip);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            //删除zip本地文件
            if (zip.exists()) {
                zip.delete();
            }
            // 清空response
            response.reset();
            // 设置response的Header
            response.setCharacterEncoding("UTF-8");
            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            //attachment表示以附件方式下载 inline表示在线打开 "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(filename, CHARACTER_SET));
            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + zipLength);
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            outputStream.write(buffer);
            outputStream.flush();
        } catch (IOException ex) {
            log.error(MeasureResponseConstant.DOWNLOAD_ERROR, ex);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    //=======================================


    /**
     * 项目批量添加图片接口（去重） .
     */
    @RequiresPermissions("anno:project:addimage")
    @ApiOperation(value = "项目批量添加图片接口")
    @Log(title = "项目批量添加图片接口", menu = "专题管理", subMenu = "项目管理", businessType = BusinessType.INSERT)
    @PostMapping("/addProjectImage")
    public R<String> add(@Validated @RequestBody ProjectImageVO pro) {
        if (pro.getProjectId() != null && pro.getImageIdList() != null) {
            ProjectListVO projectListVO = projectService.selectProjectById(pro.getProjectId());
            if (projectListVO == null) {
                return R.fail("projectId不存在");
            }
            String result = "";
            List<Slide> slideList = new ArrayList<>();
            for (Long i : pro.getImageIdList()) {
                pro.setImageId(i);
                Slide projectImage = new Slide();
                BeanUtils.copyProperties(pro, projectImage);
                projectImage.setCreateBy(SecurityUtils.getUserId());
                Image image = new Image();
                image.setImageId(i);
                List<Image> imageList = imageService.selectImageAnnotationList(image);
                if (imageList.isEmpty()) {
                    result = result + i + ",";
                    continue;
                }
                //查寻项目是否包含该图片
                List<Slide> imageInformation = slideService.selectImageExist(projectImage);
                if (imageInformation.isEmpty()) {
                    slideList.add(projectImage);
                }
            }
            if (slideList.isEmpty()) {
                return R.fail(null, ProjectConstant.ADDED);
            }
            //添加图片
            slideManage.insertProjectImage(slideList, pro.getProjectId());
            if (result.isEmpty()) {
                return R.ok(null, ProjectConstant.STRING_ADD_COMPLETE);
            }
            return R.ok(ProjectConstant.ID_IS + result + ProjectConstant.PICTURE_NON_EXISTENT,
                    ProjectConstant.STRING_ADD_COMPLETE);
        }
        return R.fail(ProjectConstant.NO_DATA_TRANSFERRED);
    }

}