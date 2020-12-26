package com.qigetech.mark.origin.mapper;

import com.qigetech.mark.origin.entity.Origin;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public interface OriginRepository extends ElasticsearchRepository<Origin,Integer> {
    Origin queryOriginById(Integer id);
}
