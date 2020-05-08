package com.islizx.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lizx
 * @date 2020-01-30 - 12:28
 */
@Data
public class Menu implements Serializable {
    private static final long serialVersionUID = 6815304403178966614L;
    private Integer id;

    private String name;

    private String url;

    private Integer level;

    private String icon;

    private Integer order;
}
