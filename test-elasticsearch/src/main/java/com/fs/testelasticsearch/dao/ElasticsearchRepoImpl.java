package com.fs.testelasticsearch.dao;

import com.fs.testelasticsearch.bean.AccountInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface ElasticsearchRepoImpl extends ElasticsearchRepository<AccountInfo, String> {
}
