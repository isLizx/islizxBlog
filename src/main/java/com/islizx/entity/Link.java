package com.islizx.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lizx
 * @date 2020-01-30 - 12:29
 */
@Data
public class Link implements Serializable {
    private Integer id;

    /**
     * 友情链接名称
     */
    private String linkName;

    /**
     * 友情链接地址
     */
    private String linkUrl;

    /**
     * 友情链接头像
     */
    private String linkPic;

    /**
     * 友情链接描述
     */
    private String linkDesc;

    /**
     * 友情链接加入时间
     */
    private Date linkCreateTime;
}
