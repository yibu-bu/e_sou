package com.yibu.domain.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "post")  // ES索引名（无需创建索引，默认会自动创建索引）
@Data
public class PostDocument {

    private String id;     // es建议使用String类型作为id
    private String title;
    private String content;
    private String author;
}
