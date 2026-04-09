package com.yibu.domain.dto;

import com.yibu.domain.entity.Post;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * ES 实体类（只存可能查询的字段，其他数据从 MySQL 中查询）
 */
@Data
@Document(indexName = "post")
public class PostEsDTO {

    /**
     * ES的id，和MySQL表中存的id相同（需要双查获取对象完整数据）
     */
    @Id // 和 ES 的 _id 字段绑定，这里使用String类型，查MySQL时再转类型
    private String id;
    /**
     * 标题
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String title;
    /**
     * 内容
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String content;
    /**
     * 标签（json数组）
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String tags;

    /**
     * 将 Post 转为 PostEsDTO
     */
    public static PostEsDTO objToDTO(Post post) {
        if (post == null) {
            return null;
        }
        PostEsDTO postEsDTO = new PostEsDTO();
        postEsDTO.setId(String.valueOf(post.getId()));
        postEsDTO.setTitle(post.getTitle());
        postEsDTO.setContent(post.getContent());
        postEsDTO.setTags(post.getTags());
        return postEsDTO;
    }
}
