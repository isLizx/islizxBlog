package com.islizx.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lizx
 * @date 2020-01-30 - 12:00
 */
@Data
public class Type implements Serializable {
    private static final long serialVersionUID = 5160366370329403551L;
    private Integer id;

    private String name;

    private String description;

    private String icon;

    private List<Blog> blogs = new ArrayList<>();

    /**
     * 文章数量(不是数据库字段)
     */
    private Integer blogCount;

    public Type(Integer id, String name, String description, String icon, Integer blogCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.blogCount = blogCount;
    }

    public Type(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Type() {
    }
}
