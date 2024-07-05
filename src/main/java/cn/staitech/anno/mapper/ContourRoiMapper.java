package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.ContourRoi;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author admin
* @description 针对表【tb_contour_roi】的数据库操作Mapper
* @createDate 2024-06-11 13:22:07
* @Entity cn.staitech.anno.domain.ContourRoi
*/
public interface ContourRoiMapper extends BaseMapper<ContourRoi> {

    int insert1(ContourRoi contourRoi);



    ContourRoi selectBy(ContourRoi contourRoi);
}




