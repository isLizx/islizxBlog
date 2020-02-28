package com.islizx.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lizx
 * @date 2020-01-30 - 12:37
 */
@Data
public class BlogTagRef implements Serializable {
    private Integer blogsId;

    private Integer tagsId;

    public BlogTagRef() {
    }

    public BlogTagRef(Integer blogsId, Integer tagsId) {
        this.blogsId = blogsId;
        this.tagsId = tagsId;
    }
}
