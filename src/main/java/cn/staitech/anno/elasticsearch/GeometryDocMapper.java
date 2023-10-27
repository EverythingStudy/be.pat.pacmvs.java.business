package cn.staitech.anno.elasticsearch;

import cn.staitech.anno.domain.document.GeometryDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface GeometryDocMapper extends ElasticsearchRepository<GeometryDoc, String> {

    List<GeometryDoc> findBySlideId(Long slideId);

}
