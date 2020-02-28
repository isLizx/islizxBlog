package com.islizx.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lizx
 * @date 2020-01-30 - 12:27
 */
@Data
public class Notice implements Serializable {
    private Integer id;

    private String title;

    private String content;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Integer order;
}
