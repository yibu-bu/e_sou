package com.yibu.repository;

import com.yibu.domain.entity.PostDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface PostRepository extends ElasticsearchRepository<PostDocument, String> {
}
