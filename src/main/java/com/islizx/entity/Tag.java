package com.islizx.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lizx
 * @date 2020-01-30 - 12:12
 */
@Data
public class Tag implements Serializable {
    private static final long serialVersionUID = -852588101326443106L;
    private Integer id;

    private String name;

    private String description;

    /**
     * 文章数量(不是数据库字段)
     */
    private Integer blogCount;

    public Tag() {
    }

    public Tag(Integer id) {
        this.id = id;
    }

    public Tag(Integer id, String name, String description, Integer blogCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.blogCount = blogCount;
    }
}
