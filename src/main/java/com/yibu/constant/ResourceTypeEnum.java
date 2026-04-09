package com.yibu.constant;

import lombok.Getter;

/**
 * 资源类型枚举
 */
@Getter
public enum ResourceTypeEnum {

    ALL("全部", "all"),
    USER("用户", "user"),
    POST("帖子", "post"),
    PICTURE("图片", "picture");

    private final String text;
    private final String value;

    ResourceTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据value获取枚举对象
     */
    public static ResourceTypeEnum getEnumByValue(String value) {
        for (ResourceTypeEnum resourceTypeEnum : values()) {
            if (resourceTypeEnum.getValue().equals(value)) {
                return resourceTypeEnum;
            }
        }
        return ALL; // 默认返回全部
    }
}
