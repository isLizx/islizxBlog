package com.islizx.entity;

import lombok.Data;

/**
 * @author lizx
 * @date 2020-02-15 - 23:30
 */
@Data
public class Slide {

    private Integer id;
    /**
     * 幻灯片名称
     */
    private String slideTitle;

    /**
     * 幻灯片链接
     */
    private String slideUrl;

    /**
     * 幻灯片图片地址
     */
    private String slidePictureUrl;

    /**
     * 排序编号
     */
    private Integer slideSort = 1;

    /**
     * 打开方式
     */
    private String slideTarget;
}
