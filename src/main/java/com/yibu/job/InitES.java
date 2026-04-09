package com.yibu.job;

import com.yibu.domain.dto.PostEsDTO;
import com.yibu.service.PostService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 一次性脚本，将MySQL中的数据写到ES
 */
@Component
public class InitES implements CommandLineRunner {

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Resource
    private PostService postService;

    /**
     * 项目启动时执行
     */
    @Override
    public void run(String... args) {
        List<PostEsDTO> postEsDTOList = postService.list().stream()
                .map(post -> {
                    PostEsDTO postEsDTO = new PostEsDTO();
                    postEsDTO.setId(String.valueOf(post.getId()));
                    postEsDTO.setTitle(post.getTitle());
                    postEsDTO.setContent(post.getContent());
                    postEsDTO.setTags(post.getTags());
                    return postEsDTO;
                }).collect(Collectors.toList());

        // 批量写入ES
        elasticsearchRestTemplate.save(postEsDTOList);
    }
}
