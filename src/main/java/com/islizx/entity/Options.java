package com.islizx.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lizx
 * @date 2020-01-30 - 12:26
 */
@Data
public class Options implements Serializable {
    private Integer id;

    private String siteTitle;

    private String siteDescrption;

    private String metaDescrption;

    private String metaKeyword;

    private String aboutsiteAvatar;

    private String aboutsiteTitle;

    private String aboutsiteContent;

    private String aboutsiteWechat;

    private String aboutsiteQq;

    private String aboutsiteGithub;

    private String aboutsiteWeibo;

    private String tongji;

    private Integer status;
}
