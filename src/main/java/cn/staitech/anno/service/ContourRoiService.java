package cn.staitech.anno.service;

import cn.staitech.anno.domain.ContourRoi;
import cn.staitech.anno.vo.geojson.Features;
import cn.staitech.anno.vo.geojson.in.ViewAddIn;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author admin
* @description 针对表【tb_contour_roi】的数据库操作Service
* @createDate 2024-06-11 13:22:07
*/
public interface ContourRoiService extends IService<ContourRoi> {

    Long insert(ViewAddIn req) throws Exception;

    int delete(String markingId) throws Exception;

    List<Features> selectList(Long slideId) throws Exception;


}
