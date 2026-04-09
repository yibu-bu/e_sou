package com.yibu.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yibu.domain.dto.PostEsDTO;
import com.yibu.domain.entity.Post;
import com.yibu.mapper.PostMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 定时任务类，每隔一段时间检查数据库 post 表是否有更新，如果有就同步到ES
 */
@Component
@Slf4j
public class UpdateToES {

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Resource
    private PostMapper postMapper;


    @Scheduled(fixedRate = 60 * 1000)  // 每分钟执行
    public void run() {
        // 获取当前五分钟前的时间
        Date fiveMinutesAgoDate = new Date(new Date().getTime() - 5 * 60 * 1000L);

        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("updateTime", fiveMinutesAgoDate);
        List<Post> postList = postMapper.selectList(queryWrapper);
        List<PostEsDTO> postEsDTOList = postList.stream().map(PostEsDTO::objToDTO)
                .collect(Collectors.toList());
        if (postList.isEmpty()) {
            log.info("no update data");
        } else {
            elasticsearchRestTemplate.save(postEsDTOList);
            log.info("updated data {}", postList.size());
        }
    }
}
