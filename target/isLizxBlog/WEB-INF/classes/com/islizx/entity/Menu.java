package com.islizx.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lizx
 * @date 2020-01-30 - 12:28
 */
@Data
public class Menu implements Serializable {
    private Integer id;

    private String name;

    private String url;

    private Integer level;

    private String icon;

    private Integer order;
}
