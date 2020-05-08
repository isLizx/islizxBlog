package com.islizx.model.dto;

import lombok.Data;

/**
 * @author lizx
 * @date 2020-02-16 - 21:10
 */
@Data
public class CountDTO {

    /**
     * 文章总数
     */
    private Integer blogCount;

    /**
     * 评论总数
     */
    private Integer commentCount;

    /**
     * 标签总数
     */
    private Integer tagCount;

    /**
     * 分类总数
     */
    private Integer typeCount;

    /**
     * 访问量统计
     */
    private Integer viewCount;
}
