package cn.staitech.anno.controller;


import cn.staitech.anno.domain.ImageCsv;
import cn.staitech.anno.domain.vo.imageCsv.ImageCsvGetVO;
import cn.staitech.anno.service.ImageCsvService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (ImageCsv)表控制层
 *
 * @author makejava
 * @since 2023-09-13 13:20:02
 */
@RestController
@RequestMapping("/imageCsv")
public class ImageCsvController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private ImageCsvService imageCsvService;

    /**
     * 分页查询所有数据
     *
     * @param page       分页对象
     * @param tbImageCsv 查询实体
     * @return 所有数据
     */
    /**
     * @param imageCsvGetVO
     * @return
     */
    @GetMapping("/list")
    public R selectAll(ImageCsvGetVO imageCsvGetVO) {

        Page<ImageCsv> page = new Page();
        ImageCsv imageCsv = new ImageCsv();
        QueryWrapper queryWrapper = new QueryWrapper<>(imageCsv);
        // 切片编号
        queryWrapper.like("image_name", imageCsvGetVO.getImageName());
        // 组别
        queryWrapper.like("group_name", imageCsvGetVO.getGroupName());
        // 性别
        queryWrapper.like("gender", imageCsvGetVO.getGender());
        // 病变类型1
        queryWrapper.like("lesion_type1", imageCsvGetVO.getLesionType());
        queryWrapper.like("lesion_type2", imageCsvGetVO.getLesionType());
        // 病变程度1
        queryWrapper.like("lesion_degree1", imageCsvGetVO.getLesionDegree());
        queryWrapper.like("lesion_degree2", imageCsvGetVO.getLesionDegree());
        queryWrapper.orderByDesc("id");

        return R.ok(this.imageCsvService.page(page, queryWrapper));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.imageCsvService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param tbImageCsv 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody ImageCsv tbImageCsv) {
        return success(this.imageCsvService.save(tbImageCsv));
    }

    /**
     * 修改数据
     *
     * @param tbImageCsv 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody ImageCsv tbImageCsv) {
        return success(this.imageCsvService.updateById(tbImageCsv));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.imageCsvService.removeByIds(idList));
    }
}

