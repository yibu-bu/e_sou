package com.yibu.elasticsearch;

import com.yibu.domain.entity.PostDocument;
import com.yibu.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class ElasticSearchTest {


    @Resource
    private PostRepository postRepository;

    /**
     * 测试 ES 增删改查
     */
    @Test
    void elasticSearchTest() {
        PostDocument post = new PostDocument();
        post.setTitle("蟹黄包秘方");
        post.setContent("不告诉你");
        post.setAuthor("海绵宝宝");
        PostDocument saved = postRepository.save(post);
        System.out.println(saved);
    }

    @Test
    void readTest() {
        postRepository.findAll().forEach(System.out::println);
    }
}
