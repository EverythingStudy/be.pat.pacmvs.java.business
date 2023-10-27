package cn.staitech.anno.service;

import cn.staitech.anno.domain.geojson.Features;
import cn.staitech.anno.domain.geojson.in.MarkingUpdateIn;
import cn.staitech.anno.domain.geojson.in.ViewAddIn;
import org.springframework.stereotype.Service;

@Service
public interface ViewerService {

    /**
     * 获取geojsonUrl
     *
     * @param geojsonUrls 文件地址,slideId 切片id
     * @return geojsonUrl
     */
    String getGeojsonUrls(String geojsonUrls, Long slideId);

    /**
     * 构建标注数据
     *
     * @param req 入参数据
     * @return geojsonUrl
     */
    Features constructAddMarking(ViewAddIn req);

    /**
     * 构建标注数据
     *
     * @param req 入参数据
     * @return geojsonUrl
     */
    Features constructUpdMarking(MarkingUpdateIn req, String measureFullName);

    boolean zipExport(String zipUrl, Long specialId) throws Exception;


}
