package com.yibu.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 图片
 */
@Data
@Builder
@AllArgsConstructor
public class Picture {

    private String title;
    private String url;
}
